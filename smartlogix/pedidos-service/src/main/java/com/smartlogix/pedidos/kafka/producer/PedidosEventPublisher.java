package com.smartlogix.pedidos.kafka.producer;

import com.smartlogix.pedidos.service.OutboxService;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class PedidosEventPublisher {
    
    private final OutboxService outboxService;
    
    public PedidosEventPublisher(OutboxService outboxService) {
        this.outboxService = outboxService;
    }
    
    public void publishPedidoCreado(Long pedidoId, Double total) {
        outboxService.guardarEvento(
            "Pedido",
            pedidoId.toString(),
            "PedidoCreado",
            Map.of("pedidoId", pedidoId, "total", total)
        );
    }
}
