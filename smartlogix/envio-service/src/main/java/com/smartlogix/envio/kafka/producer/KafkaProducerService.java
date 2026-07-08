package com.smartlogix.envio.kafka.producer;

import com.smartlogix.envio.event.EnvioCreadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KafkaProducerService {

    private static final Logger log =
            LoggerFactory.getLogger(KafkaProducerService.class);

    private static final String TOPIC_ENVIO_CREADO     = "envio-creado";
    private static final String TOPIC_ENVIO_ENTREGADO  = "envio-entregado";
    private static final String TOPIC_ENVIO_EVENTS     = "envios-events";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarEventoEnvio(EnvioCreadoEvent event) {
        kafkaTemplate.send(TOPIC_ENVIO_CREADO, String.valueOf(event.getEnvioId()), event);
        log.info("📤 Evento EnvioCreado enviado -> id={}", event.getEnvioId());
    }

    public void enviarEventoEntregado(com.smartlogix.envio.event.EnvioEntregadoEvent event) {
        kafkaTemplate.send(TOPIC_ENVIO_ENTREGADO, String.valueOf(event.getEnvioId()), event);
        log.info("📤 Evento EnvioEntregado enviado -> envioId={}, pedidoId={}", event.getEnvioId(), event.getPedidoId());
    }

    // =========================
    // 📍 TRACKING ACTUALIZADO
    // =========================
    public void enviarTrackingActualizado(Long envioId, String estado, String ubicacionActual) {
        Map<String, Object> payload = Map.of(
            "eventType", "TrackingActualizado",
            "envioId", envioId,
            "estado", estado,
            "ubicacionActual", ubicacionActual != null ? ubicacionActual : ""
        );
        kafkaTemplate.send(TOPIC_ENVIO_EVENTS, envioId.toString(), payload);
        log.info("📤 TrackingActualizado enviado -> envioId={}, estado={}", envioId, estado);
    }

    // =========================
    // ⏱️ ETA ACTUALIZADA
    // =========================
    public void enviarETAActualizada(Long envioId, String eta) {
        Map<String, Object> payload = Map.of(
            "eventType", "ETAActualizada",
            "envioId", envioId,
            "eta", eta != null ? eta : ""
        );
        kafkaTemplate.send(TOPIC_ENVIO_EVENTS, envioId.toString(), payload);
        log.info("📤 ETAActualizada enviado -> envioId={}, eta={}", envioId, eta);
    }

    // =========================
    // 🚚 TRANSPORTISTA ASIGNADO
    // =========================
    public void enviarTransportistaAsignado(Long envioId, Long transportistaId, String nombreTransportista) {
        Map<String, Object> payload = Map.of(
            "eventType", "TransportistaAsignado",
            "envioId", envioId,
            "transportistaId", transportistaId != null ? transportistaId : 0L,
            "nombreTransportista", nombreTransportista != null ? nombreTransportista : ""
        );
        kafkaTemplate.send(TOPIC_ENVIO_EVENTS, envioId.toString(), payload);
        log.info("📤 TransportistaAsignado enviado -> envioId={}, transportistaId={}", envioId, transportistaId);
    }

    // =========================
    // 📦 EVIDENCIA DE ENTREGA
    // =========================
    public void enviarEvidenciaEntrega(Long envioId, String fotoUrl, String firmaUrl) {
        Map<String, Object> payload = Map.of(
            "eventType", "EvidenciaEntregaSubida",
            "envioId", envioId,
            "fotoUrl", fotoUrl != null ? fotoUrl : "",
            "firmaUrl", firmaUrl != null ? firmaUrl : ""
        );
        kafkaTemplate.send(TOPIC_ENVIO_EVENTS, envioId.toString(), payload);
        log.info("📤 EvidenciaEntregaSubida enviado -> envioId={}", envioId);
    }
}