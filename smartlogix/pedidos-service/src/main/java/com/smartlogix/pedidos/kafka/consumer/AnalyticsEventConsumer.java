package com.smartlogix.pedidos.kafka.consumer;

import com.smartlogix.pedidos.model.AnalyticsEvent;
import com.smartlogix.pedidos.repository.AnalyticsEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsEventConsumer {
    private static final Logger log = LoggerFactory.getLogger(AnalyticsEventConsumer.class);
    private final AnalyticsEventRepository repository;

    public AnalyticsEventConsumer(AnalyticsEventRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "analytics-events", groupId = "pedidos-analytics-group")
    public void consume(String message) {
        log.info("[ANALYTICS] Evento recibido: {}", message);
        try {
            AnalyticsEvent evt = new AnalyticsEvent();
            evt.setTipoEvento("UNKNOWN");
            if (message.contains("AnalyticsProductoImpression")) {
                evt.setTipoEvento("ProductoImpression");
            } else if (message.contains("AnalyticsProductoClicked")) {
                evt.setTipoEvento("ProductoClicked");
            }
            evt.setPayload(message);
            repository.save(evt);
        } catch (Exception e) {
            log.error("[ANALYTICS] Error procesando evento", e);
        }
    }
}
