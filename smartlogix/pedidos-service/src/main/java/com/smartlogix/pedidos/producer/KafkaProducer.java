package com.smartlogix.pedidos.producer;

import com.smartlogix.pedidos.event.CompraEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private static final Logger log =
            LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String, CompraEvent> kafkaTemplate;

    private static final String TOPIC = "compras";

    public KafkaProducer(KafkaTemplate<String, CompraEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // 🔥 FIX: agregar usuarioId
    public void enviarEventoCompra(Long productoId, Integer cantidad, String usuarioId) {

        CompraEvent event = new CompraEvent(productoId, cantidad, usuarioId);

        kafkaTemplate.send(TOPIC, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("❌ Error enviando Kafka", ex);
                    } else {
                        log.info("📤 Evento Kafka enviado correctamente");
                    }
                });
    }
}