package com.smartlogix.inventory.kafka.consumer;

import com.smartlogix.inventory.event.CompraEvent;
import com.smartlogix.inventory.service.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    private final ProductoService service;

    public KafkaConsumer(ProductoService service) {
        this.service = service;
    }

    @KafkaListener(topics = "compras", groupId = "inventory-group")
    public void consumir(CompraEvent event) {

        try {
            log.info("📥 Evento recibido -> productoId={}, cantidad={}, usuarioId={}",
                    event.getProductoId(),
                    event.getCantidad(),
                    event.getUsuarioId()
            );

            service.descontarStock(
                    event.getProductoId(),
                    event.getCantidad(),
                    event.getUsuarioId()
            );

            log.info("✅ Stock actualizado correctamente");

        } catch (Exception e) {
            log.error("❌ Error procesando evento Kafka", e);

            // 🔥 IMPORTANTE: aquí luego puedes mandar a DLQ (dead letter topic)
            // o reintentos controlados
        }
    }
}