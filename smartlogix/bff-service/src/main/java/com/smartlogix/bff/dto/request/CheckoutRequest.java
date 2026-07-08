package com.smartlogix.bff.dto.request;

import java.util.List;

public class CheckoutRequest {

    private String usuarioId;

    private List<DetalleCompraRequest> productos;

    private String direccionEnvio;

    private String metodoPago;

    private String telefonoContacto;

    private String notasEntrega;

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

    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getNotasEntrega() {
        return notasEntrega;
    }

    public void setNotasEntrega(String notasEntrega) {
        this.notasEntrega = notasEntrega;
    }
}