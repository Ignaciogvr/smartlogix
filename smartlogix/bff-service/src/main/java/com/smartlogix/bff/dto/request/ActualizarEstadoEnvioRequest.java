package com.smartlogix.bff.dto.request;

public class ActualizarEstadoEnvioRequest {

    private String estado;

    private String observacion;

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
