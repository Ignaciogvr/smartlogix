package com.smartlogix.bff.dto.request;

import java.util.List;

public class CheckoutRequest {

    private String usuarioId;

    private List<DetalleCompraRequest> productos;

    private String direccionEnvio;

    private String metodoPago;

    public CheckoutRequest() {
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

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}