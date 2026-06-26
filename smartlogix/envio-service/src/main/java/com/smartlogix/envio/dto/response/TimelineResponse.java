package com.smartlogix.envio.dto.response;

import java.time.LocalDateTime;

public class TimelineResponse {

    private String estado;
    private String descripcion;

    private LocalDateTime fecha;

    // getters & setters

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}