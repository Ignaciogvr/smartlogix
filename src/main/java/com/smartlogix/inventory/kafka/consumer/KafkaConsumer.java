package com.smartlogix.inventory.kafka.consumer;

import com.smartlogix.inventory.event.CompraEvent;
import com.smartlogix.inventory.service.ProductoService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private final ProductoService service;

    public KafkaConsumer(ProductoService service) {
        this.service = service;
    }

    @KafkaListener(topics = "compras", groupId = "inventory-group")
    public void consumir(CompraEvent event) {

        try {
            System.out.println("📥 Evento recibido -> Producto: "
                    + event.getProductoId()
                    + " Cantidad: " + event.getCantidad()
                    + " Usuario: " + event.getUsuarioId());

            service.descontarStock(
                    event.getProductoId(),
                    event.getCantidad(),
                    event.getUsuarioId()
            );

        } catch (Exception e) {
            System.out.println("❌ Error procesando evento: " + e.getMessage());
        }
    }
}