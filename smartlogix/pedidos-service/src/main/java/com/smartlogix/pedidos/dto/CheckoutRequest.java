package com.smartlogix.pedidos.dto;

public class CheckoutRequest {
    private String metodoPago;
    private String direccionEnvio;
    private String telefonoContacto;
    private String notasEntrega;

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    
    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }
    
    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String telefonoContacto) { this.telefonoContacto = telefonoContacto; }
    
    public String getNotasEntrega() { return notasEntrega; }
    public void setNotasEntrega(String notasEntrega) { this.notasEntrega = notasEntrega; }
}
