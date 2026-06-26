package com.smartlogix.pedidos.event;

import java.util.List;

/**
 * Factory Pattern: centraliza la creación de eventos Kafka.
 *
 * En vez de crear eventos inline con "new" en cada service method,
 * se usa esta factory. Si cambian los constructores de los eventos,
 * solo se modifica aquí.
 */
public class PedidoEventFactory {

    private PedidoEventFactory() {
        // utility class
    }

    public static PedidoEstadoActualizadoEvent estadoActualizado(Long pedidoId, String nuevoEstado) {
        return new PedidoEstadoActualizadoEvent(pedidoId, nuevoEstado);
    }

    public static PedidoCanceladoEvent cancelado(Long pedidoId) {
        return new PedidoCanceladoEvent(pedidoId);
    }

    public static PedidoCreadoEvent creado(Long pedidoId, String usuarioId, String direccion,
                                            Double total, List<ProductoEvent> productos) {
        return new PedidoCreadoEvent(pedidoId, usuarioId, direccion, total, productos);
    }

    public static CompraEvent compra(Long productoId, Integer cantidad, String usuarioId) {
        return new CompraEvent(productoId, cantidad, usuarioId);
    }
}
