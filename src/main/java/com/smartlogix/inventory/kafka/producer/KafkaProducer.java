package com.smartlogix.inventory.kafka.producer;

import com.smartlogix.inventory.event.ProductoCreadoEvent;
import com.smartlogix.inventory.event.StockDescontadoEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // 📦 Evento: producto creado
    public void enviarProductoCreado(ProductoCreadoEvent event) {
        kafkaTemplate.send("producto-creado", event.getId().toString(), event);
    }

    // 📉 Evento: stock descontado
    public void enviarStockDescontado(StockDescontadoEvent event) {
        kafkaTemplate.send("stock-descontado",
                event.getProductoId().toString(),
                event);
    }
}