package com.smartlogix.envio.kafka.consumer;

import com.smartlogix.envio.event.PedidoCanceladoEvent;
import com.smartlogix.envio.service.EnvioService;
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
import java.util.concurrent.TimeoutException;

/**
 * Consumer que cancela un envío cuando se cancela el pedido.
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
 * - Cualquier otra excepción no listada en include
 */
@Component
public class PedidoCanceladoConsumer {

    private static final Logger log = LoggerFactory.getLogger(PedidoCanceladoConsumer.class);

    private final EnvioService envioService;

    public PedidoCanceladoConsumer(EnvioService envioService) {
        this.envioService = envioService;
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
    @KafkaListener(topics = "pedido-cancelado", groupId = "envio-group")
    public void consumirPedidoCancelado(PedidoCanceladoEvent event,
                                        @Header(value = KafkaHeaders.RECEIVED_TOPIC, required = false) String topic,
                                        @Header(value = KafkaHeaders.OFFSET, required = false) Long offset,
                                        @Header(value = "X-Request-Id", required = false) String requestId) {

        // Restaurar requestId en MDC para trazabilidad
        if (requestId != null) {
            MDC.put("requestId", requestId);
        }

        if (event == null || event.getPedidoId() == null) {
            log.error("🔴 [DLT] PedidoCanceladoEvent inválido recibido en topic={}, offset={}, requestId={}, event={}", 
                topic, offset, requestId, event);
            throw new IllegalArgumentException("Evento o pedidoId null no pueden procesarse");
        }

        log.info("🚫 [ATTEMPT] Evento pedido cancelado recibido -> topic={}, offset={}, pedidoId={}, requestId={}",
                topic, offset, event.getPedidoId(), requestId);

        // Agregar pedidoId al MDC
        MDC.put("pedidoId", event.getPedidoId().toString());

        // Cancelar envío (puede lanzar DataAccessException si BD down)
        envioService.cancelarPorPedido(event.getPedidoId());

        log.info("✅ [SUCCESS] Envío cancelado correctamente -> pedidoId={}, offset={}, requestId={}", 
            event.getPedidoId(), offset, requestId);
        
        // Limpiar MDC
        MDC.remove("pedidoId");
        MDC.remove("requestId");
    }

    /**
     * Dead Letter Topic Handler para pedido-cancelado.
     */
    @DltHandler
    public void handleDlt(PedidoCanceladoEvent event,
                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                          @Header(KafkaHeaders.OFFSET) Long offset,
                          @Header(value = KafkaHeaders.EXCEPTION_MESSAGE, required = false) String exceptionMessage,
                          @Header(value = KafkaHeaders.EXCEPTION_STACKTRACE, required = false) String stacktrace) {

        log.error("""
                ⚠️⚠️⚠️ [DEAD LETTER TOPIC - pedido-cancelado] ⚠️⚠️⚠️
                Mensaje enviado a DLT después de agotar reintentos o error permanente.
                
                Topic Original: {}
                Offset: {}
                PedidoId: {}
                
                Exception: {}
                
                Stacktrace (primeras líneas):
                {}
                
                ⚠️ ACCIÓN REQUERIDA: Revisar manualmente y reprocesar si es necesario.
                ⚠️ IMPACTO: El pedido está cancelado pero el envío NO (inconsistencia).
                """,
                topic,
                offset,
                event != null ? event.getPedidoId() : "N/A",
                exceptionMessage,
                stacktrace != null ? stacktrace.substring(0, Math.min(500, stacktrace.length())) : "N/A"
        );
    }
}
