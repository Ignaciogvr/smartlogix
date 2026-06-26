package com.smartlogix.envio.kafka.consumer;

import com.smartlogix.envio.client.PedidoClient;
import com.smartlogix.envio.client.UsuarioClient;
import com.smartlogix.envio.event.EnvioCreadoEvent;
import com.smartlogix.envio.event.PedidoCreadoEvent;
import com.smartlogix.envio.kafka.producer.KafkaProducerService;
import com.smartlogix.envio.model.Envio;
import com.smartlogix.envio.model.EstadoEnvio;
import com.smartlogix.envio.repository.EnvioRepository;
import com.smartlogix.envio.util.TrackingGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.net.ConnectException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * Consumer que crea un envío automáticamente cuando se crea un pedido.
 * 
 * ESTRATEGIA DE REINTENTOS:
 * - 4 intentos totales (1 inicial + 3 reintentos)
 * - Backoff exponencial: 2s, 4s, 8s
 * 
 * EXCEPCIONES TEMPORALES (se reintenta):
 * - SQLException, DataAccessException (BD down)
 * - ConnectException, TimeoutException (red, latencia)
 * - WebClientRequestException (HTTP timeout)
 * - WebClientResponseException.ServiceUnavailable (503)
 * - WebClientResponseException.GatewayTimeout (504)
 * - WebClientResponseException.InternalServerError (500)
 * 
 * EXCEPCIONES PERMANENTES (DLT inmediato):
 * - IllegalArgumentException (validación de negocio)
 * - WebClientResponseException.NotFound (404 - pedido/usuario no existe)
 * - WebClientResponseException.BadRequest (400)
 * - Cualquier otra excepción no listada en include
 */
@Component
public class PedidoCreadoConsumer {

    private static final Logger log = LoggerFactory.getLogger(PedidoCreadoConsumer.class);

    private final EnvioRepository envioRepository;
    private final KafkaProducerService kafkaProducerService;
    private final PedidoClient pedidoClient;
    private final UsuarioClient usuarioClient;

    public PedidoCreadoConsumer(
            EnvioRepository envioRepository,
            KafkaProducerService kafkaProducerService,
            PedidoClient pedidoClient,
            UsuarioClient usuarioClient
    ) {
        this.envioRepository = envioRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.pedidoClient = pedidoClient;
        this.usuarioClient = usuarioClient;
    }

    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(delay = 2000, multiplier = 2.0, maxDelay = 10000),
            autoCreateTopics = "true",
            include = {
                    SQLException.class,
                    DataAccessException.class,
                    ConnectException.class,
                    TimeoutException.class,
                    WebClientRequestException.class,
                    WebClientResponseException.ServiceUnavailable.class,
                    WebClientResponseException.GatewayTimeout.class,
                    WebClientResponseException.InternalServerError.class
            },
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            dltTopicSuffix = "-dlt",
            retryTopicSuffix = "-retry"
    )
    @KafkaListener(topics = "pedido-creado", groupId = "envio-group")
    public void consumirPedidoCreado(PedidoCreadoEvent event,
                                     @Header(value = KafkaHeaders.RECEIVED_TOPIC, required = false) String topic,
                                     @Header(value = KafkaHeaders.OFFSET, required = false) Long offset,
                                     @Header(value = "X-Request-Id", required = false) String requestId) {

        // Restaurar requestId en MDC para trazabilidad
        if (requestId != null) {
            MDC.put("requestId", requestId);
        }

        if (event == null || event.getPedidoId() == null || event.getUsuarioId() == null) {
            log.error("🔴 [DLT] PedidoCreadoEvent inválido recibido en topic={}, offset={}, requestId={}, event={}", 
                topic, offset, requestId, event);
            throw new IllegalArgumentException("Evento o campos null no pueden procesarse");
        }

        log.info("📦 [ATTEMPT] Evento pedido recibido -> topic={}, offset={}, pedidoId={}, usuarioId={}, requestId={}",
                topic, offset, event.getPedidoId(), event.getUsuarioId(), requestId);

        // Agregar pedidoId al MDC
        MDC.put("pedidoId", event.getPedidoId().toString());

        // IDEMPOTENCIA: Verificar si ya existe un envío para este pedido
        List<Envio> enviosExistentes = envioRepository.findByPedidoId(event.getPedidoId());
        if (!enviosExistentes.isEmpty()) {
            log.info("⏭️ Ignorando evento duplicado: El envío ya existe para pedidoId={}, offset={}", event.getPedidoId(), offset);
            return;
        }

        // VALIDAR PEDIDO (puede lanzar WebClientException si timeout o 404)
        pedidoClient.obtenerPedido(event.getPedidoId().toString());
        log.info("✅ Pedido validado -> pedidoId={}", event.getPedidoId());

        // VALIDAR USUARIO (puede lanzar WebClientException)
        Boolean existeUsuario = usuarioClient.obtenerUsuarioPorId(event.getUsuarioId());
        if (Boolean.FALSE.equals(existeUsuario)) {
            log.error("🔴 [DLT] Usuario no existe -> usuarioId={}. Enviando a DLT.", event.getUsuarioId());
            throw new IllegalArgumentException("Usuario no existe: " + event.getUsuarioId());
        }
        log.info("✅ Usuario validado -> usuarioId={}", event.getUsuarioId());

        // CREAR ENVÍO (puede lanzar DataAccessException si BD down)
        Envio envio = new Envio();
        envio.setPedidoId(event.getPedidoId());
        envio.setUsuarioId(event.getUsuarioId());
        envio.setTrackingNumber(TrackingGenerator.generate());
        envio.setEstado(EstadoEnvio.PENDIENTE);
        envio.setDireccionDestino(event.getDireccionDestino());

        Envio saved = envioRepository.save(envio);

        // Agregar envioId al MDC
        MDC.put("envioId", saved.getId().toString());

        // PUBLICAR EVENTO
        kafkaProducerService.enviarEventoEnvio(
                new EnvioCreadoEvent(
                        saved.getId(),
                        saved.getPedidoId(),
                        saved.getTrackingNumber()
                )
        );

        log.info("🚚 [SUCCESS] ENVÍO CREADO -> tracking={}, offset={}, envioId={}, requestId={}", 
            saved.getTrackingNumber(), offset, saved.getId(), requestId);
        
        // Limpiar MDC
        MDC.remove("pedidoId");
        MDC.remove("envioId");
        MDC.remove("requestId");
    }

    /**
     * Dead Letter Topic Handler para pedido-creado.
     */
    @DltHandler
    public void handleDlt(PedidoCreadoEvent event,
                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                          @Header(KafkaHeaders.OFFSET) Long offset,
                          @Header(value = KafkaHeaders.EXCEPTION_MESSAGE, required = false) String exceptionMessage,
                          @Header(value = KafkaHeaders.EXCEPTION_STACKTRACE, required = false) String stacktrace) {

        log.error("""
                ⚠️⚠️⚠️ [DEAD LETTER TOPIC - pedido-creado] ⚠️⚠️⚠️
                Mensaje enviado a DLT después de agotar reintentos o error permanente.
                
                Topic Original: {}
                Offset: {}
                PedidoId: {}
                UsuarioId: {}
                DireccionDestino: {}
                
                Exception: {}
                
                Stacktrace (primeras líneas):
                {}
                
                ⚠️ ACCIÓN REQUERIDA: Revisar manualmente y reprocesar si es necesario.
                ⚠️ IMPACTO: El pedido existe pero NO tiene envío asociado (inconsistencia).
                """,
                topic,
                offset,
                event != null ? event.getPedidoId() : "N/A",
                event != null ? event.getUsuarioId() : "N/A",
                event != null ? event.getDireccionDestino() : "N/A",
                exceptionMessage,
                stacktrace != null ? stacktrace.substring(0, Math.min(500, stacktrace.length())) : "N/A"
        );

        // Aquí podrías:
        // - Guardar en tabla de mensajes fallidos
        // - Enviar alerta (Slack, Email, PagerDuty)
        // - Incrementar métrica de Prometheus
    }
}
