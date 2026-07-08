package com.smartlogix.inventory.kafka.producer;

import com.smartlogix.inventory.service.OutboxService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InventoryEventPublisher {
    
    private final OutboxService outboxService;
    
    public InventoryEventPublisher(OutboxService outboxService) {
        this.outboxService = outboxService;
    }
    
    public void publishAnalyticsEvent(String eventType, Map<String, Object> data) {
        outboxService.guardarEvento(
            "Analytics",
            data.getOrDefault("id", "0").toString(),
            "Analytics" + eventType,
            data
        );
    }
    
    public void publishStockEvent(String eventType, Long productoId, int cantidad) {
        outboxService.guardarEvento(
            "Stock",
            productoId.toString(),
            "Stock" + eventType,
            Map.of("productoId", productoId, "cantidad", cantidad)
        );
    }
}
