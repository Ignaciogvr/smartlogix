package com.smartlogix.pedidos.event;

public class PedidoCanceladoEvent {

    private Long pedidoId;

    public PedidoCanceladoEvent() {}

    public PedidoCanceladoEvent(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
}
