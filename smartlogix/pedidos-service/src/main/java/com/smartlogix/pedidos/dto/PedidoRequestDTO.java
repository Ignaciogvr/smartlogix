package com.smartlogix.pedidos.dto;

import java.util.List;

public class PedidoRequestDTO {

    private String usuarioId; // Auth0 ID
    private List<DetallePedidoDTO> productos;

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<DetallePedidoDTO> getProductos() {
        return productos;
    }

    public void setProductos(List<DetallePedidoDTO> productos) {
        this.productos = productos;
    }
}