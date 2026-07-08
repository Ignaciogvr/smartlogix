package com.smartlogix.pedidos.event;

public class PedidoEstadoActualizadoEvent {

    private Long pedidoId;
    private String nuevoEstado;

    public PedidoEstadoActualizadoEvent() {}

    public PedidoEstadoActualizadoEvent(Long pedidoId, String nuevoEstado) {
        this.pedidoId = pedidoId;
        this.nuevoEstado = nuevoEstado;
    }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    public String getNuevoEstado() { return nuevoEstado; }
    public void setNuevoEstado(String nuevoEstado) { this.nuevoEstado = nuevoEstado; }
}



