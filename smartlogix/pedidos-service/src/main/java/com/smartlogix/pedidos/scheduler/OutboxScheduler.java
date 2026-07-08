package com.smartlogix.pedidos.scheduler;

import com.smartlogix.pedidos.model.OutboxEvent;
import com.smartlogix.pedidos.repository.OutboxEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
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

    public OutboxScheduler(OutboxEventRepository outboxRepository, @Qualifier("stringKafkaTemplate") KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void procesarEventosOutbox() {
        List<OutboxEvent> pendientes = outboxRepository.findByEstadoOrderByFechaCreacionAsc("PENDING");
        if(pendientes.isEmpty()) return;

        log.info("[OUTBOX-PEDIDOS] Procesando {} eventos pendientes", pendientes.size());

        for (OutboxEvent event : pendientes) {
            try {
                String topic = "pedidos-events";
                if (event.getEventType().startsWith("Analytics")) {
                    topic = "analytics-events";
                }

                kafkaTemplate.send(topic, event.getAggregateId(), event.getPayload());
                
                event.setEstado("PROCESSED");
                outboxRepository.save(event);
                log.info("[OUTBOX-PEDIDOS] Evento procesado id={}", event.getId());
            } catch (Exception e) {
                log.error("[OUTBOX-PEDIDOS] Error procesando evento id={}", event.getId(), e);
                event.setEstado("FAILED");
                outboxRepository.save(event);
            }
        }
    }
}
