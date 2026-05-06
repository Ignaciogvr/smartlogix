package com.smartlogix.inventory.kafka.producer;

import com.smartlogix.inventory.event.ProductoCreadoEvent;
import com.smartlogix.inventory.event.StockDescontadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // 📦 PRODUCTO CREADO
    public void enviarProductoCreado(ProductoCreadoEvent event) {

        if (event == null || event.getId() == null) return;

        kafkaTemplate.send(
                "producto-creado",
                event.getId().toString(),
                event
        );

        log.info("📤 ProductoCreadoEvent enviado -> id={}", event.getId());
    }

    // 📉 STOCK DESCONTADO
    public void enviarStockDescontado(StockDescontadoEvent event) {

        if (event == null || event.getProductoId() == null) return;

        kafkaTemplate.send(
                "stock-descontado",
                event.getProductoId().toString(),
                event
        );

        log.info("📤 StockDescontadoEvent enviado -> productoId={}", event.getProductoId());
    }
}