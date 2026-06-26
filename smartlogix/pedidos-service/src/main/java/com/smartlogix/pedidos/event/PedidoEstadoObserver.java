package com.smartlogix.pedidos.event;

import com.smartlogix.pedidos.model.Pedido;

/**
 * Observer Pattern: interfaz que define un observador de cambios de estado en pedidos.
 *
 * Los services notifican a los observers cuando un pedido cambia de estado,
 * sin conocer los detalles de la publicación (Kafka, logs, etc.).
 */
public interface PedidoEstadoObserver {

    /**
     * Notifica que un pedido cambió de estado.
     * @param pedido el pedido con el nuevo estado ya aplicado
     */
    void onEstadoCambiado(Pedido pedido);

    /**
     * Notifica que un pedido fue cancelado.
     * @param pedido el pedido cancelado
     */
    void onPedidoCancelado(Pedido pedido);

    /**
     * Notifica que un pedido fue creado.
     * @param pedido el pedido creado
     * @param direccion dirección de envío
     * @param productos lista de productos del evento
     */
    void onPedidoCreado(Pedido pedido, String direccion, java.util.List<ProductoEvent> productos);
}
