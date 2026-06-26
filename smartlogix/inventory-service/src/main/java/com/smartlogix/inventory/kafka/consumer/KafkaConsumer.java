package com.smartlogix.inventory.kafka.consumer;

import com.smartlogix.inventory.event.CompraEvent;
import com.smartlogix.inventory.service.ProductoService;
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
 * Kafka Consumer con Retry y Dead Letter Topic (DLT).
 * 
 * ESTRATEGIA DE REINTENTOS:
 * - 4 intentos totales (1 inicial + 3 reintentos)
 * - Backoff exponencial: 2s, 4s, 8s
 * - Excepciones temporales → Retry
 * - Excepciones permanentes → DLT inmediato
 * 
 * EXCEPCIONES TEMPORALES (se reintenta):
 * - SQLException, DataAccessException (BD down, timeout)
 * - ConnectException, TimeoutException (red, latencia)
 * - WebClientRequestException (HTTP timeout)
 * - WebClientResponseException.ServiceUnavailable (503)
 * - WebClientResponseException.GatewayTimeout (504)
 * - WebClientResponseException.InternalServerError (500)
 * 
 * EXCEPCIONES PERMANENTES (DLT inmediato):
 * - IllegalArgumentException (validación de negocio)
 * - NullPointerException (datos corruptos)
 * - WebClientResponseException.NotFound (404 - producto no existe)
 * - WebClientResponseException.BadRequest (400)
 * - Cualquier otra excepción no listada en include
 */
@Component
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    private final ProductoService service;

    public KafkaConsumer(ProductoService service) {
        this.service = service;
    }

    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(
                    delay = 2000,        // 2 segundos inicial
                    multiplier = 2.0,    // exponencial: 2s, 4s, 8s
                    maxDelay = 10000     // máximo 10 segundos
            ),
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
    @KafkaListener(topics = "compras", groupId = "inventory-group")
    public void consumir(CompraEvent event,
                         @Header(value = KafkaHeaders.RECEIVED_TOPIC, required = false) String topic,
                         @Header(value = KafkaHeaders.OFFSET, required = false) Long offset,
                         @Header(value = "X-Request-Id", required = false) String requestId) {

        // Restaurar requestId en MDC para trazabilidad
        if (requestId != null) {
            MDC.put("requestId", requestId);
        }

        // Validación de mensaje nulo (error permanente → DLT)
        if (event == null) {
            log.error("🔴 [DLT] Evento Kafka null recibido en topic={}, offset={}, requestId={}. Enviando a DLT.", 
                topic, offset, requestId);
            throw new IllegalArgumentException("Evento null no puede procesarse");
        }

        // Validar campos obligatorios del evento
        if (event.getProductoId() == null) {
            log.error("🔴 [DLT] ProductoId null en evento: topic={}, offset={}, requestId={}", topic, offset, requestId);
            throw new IllegalArgumentException("ProductoId null no puede procesarse");
        }

        if (event.getCantidad() == null || event.getCantidad() <= 0) {
            log.error("🔴 [DLT] Cantidad inválida en evento: cantidad={}, topic={}, offset={}, requestId={}", 
                event.getCantidad(), topic, offset, requestId);
            throw new IllegalArgumentException("Cantidad inválida: " + event.getCantidad());
        }

        log.info("📥 [ATTEMPT] CompraEvent recibido -> topic={}, offset={}, productoId={}, cantidad={}, usuarioId={}, requestId={}",
                topic,
                offset,
                event.getProductoId(),
                event.getCantidad(),
                event.getUsuarioId(),
                requestId
        );

        // Agregar productoId al MDC
        MDC.put("productoId", event.getProductoId().toString());

        // Proceso de negocio (puede lanzar excepciones temporales o permanentes)
        service.descontarStock(
                event.getProductoId(),
                event.getCantidad(),
                event.getUsuarioId()
        );

        log.info("✅ [SUCCESS] Stock actualizado correctamente -> productoId={}, offset={}, requestId={}", 
                event.getProductoId(), offset, requestId);
        
        // Limpiar MDC
        MDC.remove("productoId");
        MDC.remove("requestId");
    }

    /**
     * Dead Letter Topic Handler.
     * Se ejecuta cuando:
     * 1. Se agotaron todos los reintentos (4 intentos)
     * 2. Se lanzó una excepción permanente (IllegalArgumentException, NullPointer, etc.)
     */
    @DltHandler
    public void handleDlt(CompraEvent event,
                          @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                          @Header(KafkaHeaders.OFFSET) Long offset,
                          @Header(value = KafkaHeaders.EXCEPTION_MESSAGE, required = false) String exceptionMessage,
                          @Header(value = KafkaHeaders.EXCEPTION_STACKTRACE, required = false) String stacktrace) {

        log.error("""
                ⚠️⚠️⚠️ [DEAD LETTER TOPIC] ⚠️⚠️⚠️
                Mensaje enviado a DLT después de agotar reintentos o error permanente.
                
                Topic Original: {}
                Offset: {}
                ProductoId: {}
                Cantidad: {}
                UsuarioId: {}
                
                Exception: {}
                
                Stacktrace (primeras líneas):
                {}
                
                ⚠️ ACCIÓN REQUERIDA: Revisar manualmente este mensaje y reprocesar si es necesario.
                """,
                topic,
                offset,
                event != null ? event.getProductoId() : "N/A",
                event != null ? event.getCantidad() : "N/A",
                event != null ? event.getUsuarioId() : "N/A",
                exceptionMessage,
                stacktrace != null ? stacktrace.substring(0, Math.min(500, stacktrace.length())) : "N/A"
        );

        // Aquí podrías:
        // 1. Guardar en una tabla de "mensajes fallidos" para reprocesamiento manual
        // 2. Enviar alerta a Slack/Email
        // 3. Guardar en S3 para análisis posterior
        // 4. Incrementar métrica de Prometheus

        // Ejemplo: guardar en BD (opcional)
        // failedMessageRepository.save(new FailedMessage(event, exceptionMessage, Instant.now()));
    }
}