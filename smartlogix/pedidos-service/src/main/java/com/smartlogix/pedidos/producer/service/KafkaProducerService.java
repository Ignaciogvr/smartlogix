package com.smartlogix.pedidos.producer.service;

import com.smartlogix.pedidos.event.CompraEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final Logger log =
            LoggerFactory.getLogger(KafkaProducerService.class);

    private final KafkaTemplate<String, CompraEvent> kafkaTemplate;

    private static final String TOPIC = "compras";

    public KafkaProducerService(KafkaTemplate<String, CompraEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarEventoCompra(Long productoId, Integer cantidad, String usuarioId) {

        CompraEvent event = new CompraEvent(productoId, cantidad, usuarioId);

        kafkaTemplate.send(TOPIC, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("❌ Error enviando evento Kafka", ex);
                    } else {
                        log.info("📤 Evento Kafka enviado OK: productoId={}, cantidad={}, usuarioId={}",
                                productoId, cantidad, usuarioId);
                    }
                });
    }
}