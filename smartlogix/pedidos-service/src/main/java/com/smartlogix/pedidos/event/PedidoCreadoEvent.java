package com.smartlogix.pedidos.event;

import java.util.List;

public class PedidoCreadoEvent {

    private Long pedidoId;
    private String usuarioId;
    private String direccionDestino;
    private Double total;
    private List<ProductoEvent> productos;

    public PedidoCreadoEvent() {}

    public PedidoCreadoEvent(Long pedidoId, String usuarioId, String direccionDestino, Double total, List<ProductoEvent> productos) {
        this.pedidoId = pedidoId;
        this.usuarioId = usuarioId;
        this.direccionDestino = direccionDestino;
        this.total = total;
        this.productos = productos;
    }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    public String getDireccionDestino() { return direccionDestino; }
    public void setDireccionDestino(String direccionDestino) { this.direccionDestino = direccionDestino; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    public List<ProductoEvent> getProductos() { return productos; }
    public void setProductos(List<ProductoEvent> productos) { this.productos = productos; }
}
