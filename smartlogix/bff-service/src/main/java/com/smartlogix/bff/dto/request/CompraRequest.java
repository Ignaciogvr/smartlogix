package com.smartlogix.bff.dto.request;

import java.util.List;

public class CompraRequest {

    private String usuarioId;

    private List<DetalleCompraRequest> productos;

    public CompraRequest() {
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<DetalleCompraRequest> getProductos() {
        return productos;
    }

    public void setProductos(List<DetalleCompraRequest> productos) {
        this.productos = productos;
    }

    private String direccionDestino;

    public String getDireccionDestino() { return direccionDestino; }
    public void setDireccionDestino(String direccionDestino) { this.direccionDestino = direccionDestino; }
}