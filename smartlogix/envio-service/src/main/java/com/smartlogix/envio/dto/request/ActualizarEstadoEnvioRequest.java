package com.smartlogix.envio.dto.request;

public class ActualizarEstadoEnvioRequest {

    private String estado; // PREPARANDO, EN_RUTA, ENTREGADO, CANCELADO

    private String observacion;

    // getters & setters

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}