package com.smartlogix.pedidos.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogix.pedidos.event.EnvioEntregadoEvent;
import com.smartlogix.pedidos.model.EstadoPedido;
import com.smartlogix.pedidos.model.Pedido;
import com.smartlogix.pedidos.repository.PedidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EnvioEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(EnvioEventConsumer.class);
    private final PedidoRepository pedidoRepository;
    private final ObjectMapper objectMapper;

    public EnvioEventConsumer(PedidoRepository pedidoRepository, ObjectMapper objectMapper) {
        this.pedidoRepository = pedidoRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "envio-entregado", groupId = "pedidos-group")
    public void consumeEnvioEntregado(String message) {
        log.info("[KAFKA CONSUMER] Evento envio-entregado recibido: {}", message);
        try {
            EnvioEntregadoEvent event = objectMapper.readValue(message, EnvioEntregadoEvent.class);
            
            if (event.getPedidoId() != null) {
                Pedido pedido = pedidoRepository.findById(event.getPedidoId()).orElse(null);
                if (pedido != null && pedido.getEstado() != EstadoPedido.ENTREGADO) {
                    pedido.setEstado(EstadoPedido.ENTREGADO);
                    pedido.setFechaEntrega(LocalDateTime.now());
                    pedidoRepository.save(pedido);
                    log.info("✅ Pedido {} marcado como ENTREGADO a raíz del envío {}", pedido.getId(), event.getEnvioId());
                } else {
                    log.warn("Pedido {} no encontrado o ya estaba entregado", event.getPedidoId());
                }
            } else {
                log.warn("El evento envio-entregado no contiene pedidoId");
            }
        } catch (Exception e) {
            log.error("[KAFKA CONSUMER] Error procesando evento envio-entregado", e);
        }
    }
}
