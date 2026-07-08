package com.smartlogix.inventory.event;

// 📦 Evento de Kafka que representa una compra 
public class CompraEvent {

    private Long productoId;
    private Integer cantidad;
    private String usuarioId;

    public CompraEvent() {}

    public CompraEvent(Long productoId, Integer cantidad, String usuarioId) {
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.usuarioId = usuarioId;
    }

    public Long getProductoId() { return productoId; }
    public Integer getCantidad() { return cantidad; }
    public String getUsuarioId() { return usuarioId; }

    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
}