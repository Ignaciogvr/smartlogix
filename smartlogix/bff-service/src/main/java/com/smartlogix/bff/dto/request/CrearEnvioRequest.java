package com.smartlogix.bff.dto.request;

public class CrearEnvioRequest {

    private Long pedidoId;

    /**
     * Auth0 subject; obligatorio para el microservicio de envíos.
     */
    private String usuarioId;

    private String direccionDestino;

    public CrearEnvioRequest() {
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }
}