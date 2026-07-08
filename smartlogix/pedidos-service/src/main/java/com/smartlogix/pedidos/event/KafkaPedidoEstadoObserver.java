package com.smartlogix.pedidos.event;

import com.smartlogix.pedidos.kafka.producer.KafkaProducerService;
import com.smartlogix.pedidos.model.Pedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Observer Pattern + Factory Pattern:
 * Implementación concreta que publica eventos vía Kafka.
 *
 * Usa PedidoEventFactory para crear los eventos.
 * El service no conoce Kafka — solo llama al observer.
 */
@Component
public class KafkaPedidoEstadoObserver implements PedidoEstadoObserver {

    private static final Logger log = LoggerFactory.getLogger(KafkaPedidoEstadoObserver.class);
    private final KafkaProducerService producer;

    public KafkaPedidoEstadoObserver(KafkaProducerService producer) {
        this.producer = producer;
    }

    @Override
    public void onEstadoCambiado(Pedido pedido) {
        log.info("🔔 Observer: estado cambiado pedidoId={} → {}",
                pedido.getId(), pedido.getEstado().name());
        producer.enviarEventoPedidoEstadoActualizado(
                pedido.getId(),
                pedido.getEstado().name()
        );
    }

    @Override
    public void onPedidoCancelado(Pedido pedido) {
        log.info("🔔 Observer: pedido cancelado pedidoId={}", pedido.getId());
        producer.enviarEventoPedidoCancelado(pedido.getId());
    }

    @Override
    public void onPedidoCreado(Pedido pedido, String direccion, List<ProductoEvent> productos) {
        log.info("🔔 Observer: pedido creado pedidoId={}", pedido.getId());
        producer.enviarEventoPedidoCreado(
                PedidoEventFactory.creado(
                        pedido.getId(),
                        pedido.getUsuarioId(),
                        direccion,
                        pedido.getTotal(),
                        productos
                )
        );
    }
}
