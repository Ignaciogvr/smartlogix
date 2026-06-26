package com.smartlogix.envio.kafka.producer;

import com.smartlogix.envio.event.EnvioCreadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final Logger log =
            LoggerFactory.getLogger(KafkaProducerService.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarEventoEnvio(EnvioCreadoEvent event) {

        kafkaTemplate.send(
                "envio-creado",
                String.valueOf(event.getEnvioId()),
                event
        );

        log.info("📤 Evento EnvioCreado enviado -> id={}", event.getEnvioId());
    }
}