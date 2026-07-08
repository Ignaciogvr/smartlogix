package com.smartlogix.envio.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogix.envio.event.PedidoCreadoEvent;
import com.smartlogix.envio.service.EnvioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoCreadoConsumer {

    private static final Logger log = LoggerFactory.getLogger(PedidoCreadoConsumer.class);
    private final EnvioService envioService;
    private final ObjectMapper objectMapper;

    public PedidoCreadoConsumer(EnvioService envioService, ObjectMapper objectMapper) {
        this.envioService = envioService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "pedido-creado", groupId = "envio-group")
    public void consumePedidoCreado(String message) {
        log.info("[KAFKA CONSUMER] Evento pedido-creado recibido: {}", message);
        try {
            PedidoCreadoEvent event = objectMapper.readValue(message, PedidoCreadoEvent.class);
            
            if (event.getPedidoId() != null) {
                log.info("Procesando creación automática de envío para pedidoId={}", event.getPedidoId());
                envioService.crearEnvioInterno(
                        event.getPedidoId(), 
                        event.getUsuarioId(), 
                        event.getDireccionDestino() != null ? event.getDireccionDestino() : "N/A"
                );
            } else {
                log.warn("El evento pedido-creado no contiene pedidoId válido");
            }
        } catch (Exception e) {
            log.error("[KAFKA CONSUMER] Error procesando evento pedido-creado", e);
        }
    }
}
