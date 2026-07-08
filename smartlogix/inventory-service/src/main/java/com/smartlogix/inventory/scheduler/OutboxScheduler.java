package com.smartlogix.inventory.scheduler;

import com.smartlogix.inventory.model.OutboxEvent;
import com.smartlogix.inventory.repository.OutboxEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class OutboxScheduler {

    private static final Logger log = LoggerFactory.getLogger(OutboxScheduler.class);

    private final OutboxEventRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxScheduler(OutboxEventRepository outboxRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void procesarEventosOutbox() {
        List<OutboxEvent> pendientes = outboxRepository.findByEstadoOrderByFechaCreacionAsc("PENDING");
        if(pendientes.isEmpty()) return;

        log.info("[OUTBOX-INVENTORY] Procesando {} eventos pendientes", pendientes.size());

        for (OutboxEvent event : pendientes) {
            try {
                String topic = "inventory-events"; // default topic
                if (event.getEventType().startsWith("Analytics")) {
                    topic = "analytics-events";
                } else if (event.getEventType().startsWith("Stock")) {
                    topic = "stock-events";
                }

                kafkaTemplate.send(topic, event.getAggregateId(), event.getPayload());
                
                event.setEstado("PROCESSED");
                outboxRepository.save(event);
                log.info("[OUTBOX-INVENTORY] Evento procesado id={}", event.getId());
            } catch (Exception e) {
                log.error("[OUTBOX-INVENTORY] Error procesando evento id={}", event.getId(), e);
                event.setEstado("FAILED");
                outboxRepository.save(event);
            }
        }
    }
}
