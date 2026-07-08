package com.smartlogix.usuarios.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KafkaProducerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);

    private static final String TOPIC_USUARIOS = "usuarios-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarUsuarioRegistrado(Long usuarioId, String auth0Id, String email) {
        Map<String, Object> payload = Map.of(
            "eventType", "UsuarioRegistrado",
            "usuarioId", usuarioId,
            "auth0Id", auth0Id != null ? auth0Id : "",
            "email", email != null ? email : ""
        );
        kafkaTemplate.send(TOPIC_USUARIOS, auth0Id, payload);
        log.info("📤 Evento UsuarioRegistrado enviado -> auth0Id={}, email={}", auth0Id, email);
    }

    public void enviarPerfilActualizado(Long usuarioId, String auth0Id) {
        Map<String, Object> payload = Map.of(
            "eventType", "PerfilActualizado",
            "usuarioId", usuarioId,
            "auth0Id", auth0Id != null ? auth0Id : ""
        );
        kafkaTemplate.send(TOPIC_USUARIOS, auth0Id, payload);
        log.info("📤 Evento PerfilActualizado enviado -> auth0Id={}", auth0Id);
    }
}
