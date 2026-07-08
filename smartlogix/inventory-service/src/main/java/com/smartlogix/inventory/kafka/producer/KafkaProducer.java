package com.smartlogix.inventory.kafka.producer;

import com.smartlogix.inventory.event.ProductoCreadoEvent;
import com.smartlogix.inventory.event.StockDescontadoEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // =========================
    // 📦 PRODUCTO CREADO
    // =========================
    public void enviarProductoCreado(ProductoCreadoEvent event) {

        if (event == null || event.getId() == null) {
            log.warn("⚠️ ProductoCreadoEvent inválido");
            return;
        }

        String requestId = MDC.get("requestId");
        
        ProducerRecord<String, Object> record = new ProducerRecord<>(
                "producto-creado",
                event.getId().toString(),
                event
        );
        
        // Propagar X-Request-Id en headers de Kafka
        if (requestId != null) {
            record.headers().add("X-Request-Id", requestId.getBytes(StandardCharsets.UTF_8));
        }

        kafkaTemplate.send(record);

        log.info("📤 ProductoCreadoEvent enviado -> id={}, requestId={}", event.getId(), requestId);
    }

    // =========================
    // 📉 STOCK DESCONTADO
    // =========================
    public void enviarStockDescontado(StockDescontadoEvent event) {

        if (event == null || event.getProductoId() == null) {
            log.warn("⚠️ StockDescontadoEvent inválido");
            return;
        }

        String requestId = MDC.get("requestId");
        
        ProducerRecord<String, Object> record = new ProducerRecord<>(
                "stock-descontado",
                event.getProductoId().toString(),
                event
        );
        
        // Propagar X-Request-Id en headers de Kafka
        if (requestId != null) {
            record.headers().add("X-Request-Id", requestId.getBytes(StandardCharsets.UTF_8));
        }

        kafkaTemplate.send(record);

        log.info("📤 StockDescontadoEvent enviado -> productoId={}, requestId={}", 
            event.getProductoId(), requestId);
    }

    // =========================
    // ⚠️ STOCK BAJO
    // =========================
    public void enviarStockBajo(Long productoId, int stockActual, int stockMinimo) {
        String requestId = MDC.get("requestId");
        ProducerRecord<String, Object> record = new ProducerRecord<>(
            "stock-alertas",
            productoId.toString(),
            java.util.Map.of(
                "eventType", "StockBajo",
                "productoId", productoId,
                "stockActual", stockActual,
                "stockMinimo", stockMinimo
            )
        );
        if (requestId != null) {
            record.headers().add("X-Request-Id", requestId.getBytes(StandardCharsets.UTF_8));
        }
        kafkaTemplate.send(record);
        log.warn("⚠️ StockBajo enviado -> productoId={}, stockActual={}", productoId, stockActual);
    }

    // =========================
    // 🚨 PRODUCTO AGOTADO
    // =========================
    public void enviarProductoAgotado(Long productoId) {
        String requestId = MDC.get("requestId");
        ProducerRecord<String, Object> record = new ProducerRecord<>(
            "stock-alertas",
            productoId.toString(),
            java.util.Map.of("eventType", "ProductoAgotado", "productoId", productoId)
        );
        if (requestId != null) {
            record.headers().add("X-Request-Id", requestId.getBytes(StandardCharsets.UTF_8));
        }
        kafkaTemplate.send(record);
        log.warn("🚨 ProductoAgotado enviado -> productoId={}", productoId);
    }

    // =========================
    // ✅ PRODUCTO REPUESTO
    // =========================
    public void enviarProductoRepuesto(Long productoId, int nuevoStock) {
        String requestId = MDC.get("requestId");
        ProducerRecord<String, Object> record = new ProducerRecord<>(
            "stock-alertas",
            productoId.toString(),
            java.util.Map.of("eventType", "ProductoRepuesto", "productoId", productoId, "nuevoStock", nuevoStock)
        );
        if (requestId != null) {
            record.headers().add("X-Request-Id", requestId.getBytes(StandardCharsets.UTF_8));
        }
        kafkaTemplate.send(record);
        log.info("✅ ProductoRepuesto enviado -> productoId={}, nuevoStock={}", productoId, nuevoStock);
    }

    // =========================
    // 💬 COMENTARIOS
    // =========================
    public void enviarComentarioCreado(Long comentarioId, Long productoId, String usuarioId) {
        String requestId = MDC.get("requestId");
        ProducerRecord<String, Object> record = new ProducerRecord<>(
            "comentarios-events",
            comentarioId.toString(),
            java.util.Map.of("eventType", "ComentarioCreado", "comentarioId", comentarioId, "productoId", productoId, "usuarioId", usuarioId != null ? usuarioId : "")
        );
        if (requestId != null) {
            record.headers().add("X-Request-Id", requestId.getBytes(StandardCharsets.UTF_8));
        }
        kafkaTemplate.send(record);
        log.info("💬 ComentarioCreado enviado -> comentarioId={}", comentarioId);
    }

    public void enviarComentarioReportado(Long reporteId, Long comentarioId, String usuarioId) {
        String requestId = MDC.get("requestId");
        ProducerRecord<String, Object> record = new ProducerRecord<>(
            "comentarios-events",
            reporteId.toString(),
            java.util.Map.of("eventType", "ComentarioReportado", "reporteId", reporteId, "comentarioId", comentarioId, "usuarioId", usuarioId != null ? usuarioId : "")
        );
        if (requestId != null) {
            record.headers().add("X-Request-Id", requestId.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        }
        kafkaTemplate.send(record);
        log.warn("?? ComentarioReportado enviado -> reporteId={}, comentarioId={}", reporteId, comentarioId);
    }
}
