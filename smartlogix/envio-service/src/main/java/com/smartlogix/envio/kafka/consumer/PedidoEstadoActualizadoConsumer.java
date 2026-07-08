package com.smartlogix.envio.kafka.consumer;

import com.smartlogix.envio.event.PedidoEstadoActualizadoEvent;
import com.smartlogix.envio.model.Envio;
import com.smartlogix.envio.model.EstadoEnvio;
import com.smartlogix.envio.repository.EnvioRepository;
import com.smartlogix.envio.strategy.EstadoEnvioStrategy;
import com.smartlogix.envio.strategy.EstadoEnvioStrategyResolver;
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
 * Consumer que actualiza el estado del envío basado en eventos de pedidos-service.
 *
 * Usa Strategy Pattern (EstadoEnvioStrategyResolver) para mapeo de estados.
 * Usa idempotencia basada en level() para prevenir retroceso de estados.
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
 * - IllegalArgumentException (validación de negocio, estado desconocido)
 * - Cualquier otra excepción no listada en include
 */
@Component
public class PedidoEstadoActualizadoConsumer {

    private static final Logger log = LoggerFactory.getLogger(PedidoEstadoActualizadoConsumer.class);
    private final EnvioRepository envioRepository;

    public PedidoEstadoActualizadoConsumer(EnvioRepository envioRepository) {
        this.envioRepository = envioRepository;
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
    @KafkaListener(topics = "pedido-estado-actualizado", groupId = "envio-group")
    public void consumirPedidoEstadoActualizado(PedidoEstadoActualizadoEvent event,
                                                @Header(value = KafkaHeaders.RECEIVED_TOPIC, required = false) String topic,
                                                @Header(value = KafkaHeaders.OFFSET, required = false) Long offset,
                                                @Header(value = "X-Request-Id", required = false) String requestId) {

        // Restaurar requestId en MDC para trazabilidad
        if (requestId != null) {
            MDC.put("requestId", requestId);
        }

        if (event == null || event.getPedidoId() == null || event.getNuevoEstado() == null) {
            log.error("🔴 [DLT] PedidoEstadoActualizadoEvent inválido recibido en topic={}, offset={}, requestId={}, event={}", 
                topic, offset, requestId, event);
            throw new IllegalArgumentException("Evento o campos null no pueden procesarse");
        }

        log.info("📦 [ATTEMPT] Evento pedido‑estado‑actualizado recibido -> topic={}, offset={}, pedidoId={}, nuevoEstado={}, requestId={}",
                topic, offset, event.getPedidoId(), event.getNuevoEstado(), requestId);

        // Agregar pedidoId al MDC
        MDC.put("pedidoId", event.getPedidoId().toString());

        // Buscar envíos asociados al pedido (puede lanzar DataAccessException)
        List<Envio> envios = envioRepository.findByPedidoId(event.getPedidoId());
        if (envios.isEmpty()) {
            log.warn("⚠️ No se encontró envío para el pedidoId={}. Offset={}", event.getPedidoId(), offset);
            return;  // No es error crítico, simplemente no hay envío aún
        }

        // Strategy Pattern: resolver mapeo pedido → envío
        EstadoEnvioStrategy strategy = EstadoEnvioStrategyResolver.resolver(event.getNuevoEstado());
        if (strategy == null) {
            log.error("🔴 [DLT] Estado desconocido recibido: {}. Enviando a DLT.", event.getNuevoEstado());
            throw new IllegalArgumentException("Estado desconocido: " + event.getNuevoEstado());
        }

        EstadoEnvio nuevoEstado = EstadoEnvio.valueOf(strategy.estado());

        for (Envio envio : envios) {
            EstadoEnvio actualEstado = envio.getEstado();

            // Idempotencia: evitar retroceso de estado
            if (actualEstado != null && actualEstado.level() >= nuevoEstado.level()) {
                log.info("⏭️ Ignoro retroceso de estado: actual={}, nuevo={}, offset={}", 
                        actualEstado, nuevoEstado, offset);
                continue;
            }

            // Aplicar strategy (puede lanzar DataAccessException)
            strategy.procesar(envio);
            envioRepository.save(envio);
            log.info("⚙️ [SUCCESS] Estado envío {} actualizado a {} (pedidoId={}, offset={}, requestId={})",
                    envio.getTrackingNumber(), strategy.estado(), event.getPedidoId(), offset, requestId);
        }
        
        // Limpiar MDC
        MDC.remove("pedidoId");
        MDC.remove("requestId");
    }

    /**
     * Dead Letter Topic Handler para pedido-estado-actualizado.
     */
    @DltHandler
    public void handleDlt(PedidoEstadoActualizadoEvent event,
                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                          @Header(KafkaHeaders.OFFSET) Long offset,
                          @Header(value = KafkaHeaders.EXCEPTION_MESSAGE, required = false) String exceptionMessage,
                          @Header(value = KafkaHeaders.EXCEPTION_STACKTRACE, required = false) String stacktrace) {

        log.error("""
                ⚠️⚠️⚠️ [DEAD LETTER TOPIC - pedido-estado-actualizado] ⚠️⚠️⚠️
                Mensaje enviado a DLT después de agotar reintentos o error permanente.
                
                Topic Original: {}
                Offset: {}
                PedidoId: {}
                NuevoEstado: {}
                
                Exception: {}
                
                Stacktrace (primeras líneas):
                {}
                
                ⚠️ ACCIÓN REQUERIDA: Revisar manualmente y reprocesar si es necesario.
                ⚠️ IMPACTO: El estado del pedido cambió pero el estado del envío NO (inconsistencia).
                """,
                topic,
                offset,
                event != null ? event.getPedidoId() : "N/A",
                event != null ? event.getNuevoEstado() : "N/A",
                exceptionMessage,
                stacktrace != null ? stacktrace.substring(0, Math.min(500, stacktrace.length())) : "N/A"
        );
    }
}
