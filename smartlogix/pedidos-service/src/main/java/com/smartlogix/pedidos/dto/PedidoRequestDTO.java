package com.smartlogix.pedidos.dto;

import java.util.List;

public class PedidoRequestDTO {

    private String usuarioId;
    private List<DetallePedidoDTO> productos;
    private String direccionDestino;

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public List<DetallePedidoDTO> getProductos() { return productos; }
    public void setProductos(List<DetallePedidoDTO> productos) { this.productos = productos; }

    public String getDireccionDestino() { return direccionDestino; }
    public void setDireccionDestino(String direccionDestino) { this.direccionDestino = direccionDestino; }
}