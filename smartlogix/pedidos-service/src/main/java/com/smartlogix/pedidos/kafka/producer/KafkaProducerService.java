package com.smartlogix.pedidos.kafka.producer;

import com.smartlogix.pedidos.event.CompraEvent;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class KafkaProducerService {

    private static final Logger log =
            LoggerFactory.getLogger(KafkaProducerService.class);

    private final KafkaTemplate<String, CompraEvent> kafkaTemplate;
    private final KafkaTemplate<String, com.smartlogix.pedidos.event.PedidoCreadoEvent> pedidoCreadoKafkaTemplate;
    private final KafkaTemplate<String, com.smartlogix.pedidos.event.PedidoCanceladoEvent> pedidoCanceladoKafkaTemplate;
    private final KafkaTemplate<String, com.smartlogix.pedidos.event.PedidoEstadoActualizadoEvent> pedidoEstadoActualizadoKafkaTemplate;

    private static final String TOPIC = "compras";
    private static final String TOPIC_PEDIDO_CREADO = "pedido-creado";
    private static final String TOPIC_PEDIDO_CANCELADO = "pedido-cancelado";
    private static final String TOPIC_PEDIDO_ESTADO = "pedido-estado-actualizado";

    public KafkaProducerService(
            KafkaTemplate<String, CompraEvent> kafkaTemplate,
            KafkaTemplate<String, com.smartlogix.pedidos.event.PedidoCreadoEvent> pedidoCreadoKafkaTemplate,
            KafkaTemplate<String, com.smartlogix.pedidos.event.PedidoCanceladoEvent> pedidoCanceladoKafkaTemplate,
            KafkaTemplate<String, com.smartlogix.pedidos.event.PedidoEstadoActualizadoEvent> pedidoEstadoActualizadoKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.pedidoCreadoKafkaTemplate = pedidoCreadoKafkaTemplate;
        this.pedidoCanceladoKafkaTemplate = pedidoCanceladoKafkaTemplate;
        this.pedidoEstadoActualizadoKafkaTemplate = pedidoEstadoActualizadoKafkaTemplate;
    }

    public void enviarEventoCompra(Long productoId, Integer cantidad, String usuarioId) {

        CompraEvent event = new CompraEvent(productoId, cantidad, usuarioId);
        String requestId = MDC.get("requestId");

        ProducerRecord<String, CompraEvent> record = new ProducerRecord<>(TOPIC, event);
        
        // Propagar X-Request-Id en headers de Kafka
        if (requestId != null) {
            record.headers().add("X-Request-Id", requestId.getBytes(StandardCharsets.UTF_8));
        }

        kafkaTemplate.send(record)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("❌ Error enviando evento Kafka", ex);
                    } else {
                        log.info("📤 Evento Kafka enviado OK: productoId={}, cantidad={}, usuarioId={}, requestId={}",
                                productoId, cantidad, usuarioId, requestId);
                    }
                });
    }

    public void enviarEventoPedidoCreado(com.smartlogix.pedidos.event.PedidoCreadoEvent event) {
        String requestId = MDC.get("requestId");
        
        ProducerRecord<String, com.smartlogix.pedidos.event.PedidoCreadoEvent> record = 
            new ProducerRecord<>(TOPIC_PEDIDO_CREADO, event);
        
        // Propagar X-Request-Id en headers de Kafka
        if (requestId != null) {
            record.headers().add("X-Request-Id", requestId.getBytes(StandardCharsets.UTF_8));
        }
        
        pedidoCreadoKafkaTemplate.send(record)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("❌ Error enviando evento pedido-creado", ex);
                    } else {
                        log.info("📤 Evento pedido-creado enviado OK: pedidoId={}, requestId={}", 
                            event.getPedidoId(), requestId);
                    }
                });
    }

    public void enviarEventoPedidoCancelado(Long pedidoId) {
        com.smartlogix.pedidos.event.PedidoCanceladoEvent event = 
            new com.smartlogix.pedidos.event.PedidoCanceladoEvent(pedidoId);
        String requestId = MDC.get("requestId");
        
        ProducerRecord<String, com.smartlogix.pedidos.event.PedidoCanceladoEvent> record = 
            new ProducerRecord<>(TOPIC_PEDIDO_CANCELADO, event);
        
        // Propagar X-Request-Id en headers de Kafka
        if (requestId != null) {
            record.headers().add("X-Request-Id", requestId.getBytes(StandardCharsets.UTF_8));
        }
        
        pedidoCanceladoKafkaTemplate.send(record)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("❌ Error enviando evento pedido-cancelado", ex);
                    } else {
                        log.info("📤 Evento pedido-cancelado enviado OK: pedidoId={}, requestId={}", 
                            pedidoId, requestId);
                    }
                });
    }

    public void enviarEventoPedidoEstadoActualizado(Long pedidoId, String nuevoEstado) {
        com.smartlogix.pedidos.event.PedidoEstadoActualizadoEvent event = 
            new com.smartlogix.pedidos.event.PedidoEstadoActualizadoEvent(pedidoId, nuevoEstado);
        String requestId = MDC.get("requestId");
        
        ProducerRecord<String, com.smartlogix.pedidos.event.PedidoEstadoActualizadoEvent> record = 
            new ProducerRecord<>(TOPIC_PEDIDO_ESTADO, event);
        
        // Propagar X-Request-Id en headers de Kafka
        if (requestId != null) {
            record.headers().add("X-Request-Id", requestId.getBytes(StandardCharsets.UTF_8));
        }
        
        pedidoEstadoActualizadoKafkaTemplate.send(record)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("❌ Error enviando evento pedido-estado-actualizado", ex);
                    } else {
                        log.info("📤 Evento pedido-estado-actualizado enviado OK: pedidoId={}, nuevoEstado={}, requestId={}", 
                            pedidoId, nuevoEstado, requestId);
                    }
                });
    }
}