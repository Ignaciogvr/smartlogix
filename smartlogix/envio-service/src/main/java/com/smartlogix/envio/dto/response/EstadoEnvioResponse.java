package com.smartlogix.envio.dto.response;

import java.time.LocalDateTime;

public class EstadoEnvioResponse {

    private String estado;
    private String observacion;

    private LocalDateTime fechaCambio;

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

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }
}