package com.smartlogix.pedidos.dto;

import com.smartlogix.pedidos.model.EstadoDevolucion;
import jakarta.validation.constraints.NotNull;

public class DevolucionEstadoUpdateDTO {

    @NotNull
    private EstadoDevolucion nuevoEstado;

    private String observaciones;

    public EstadoDevolucion getNuevoEstado() { return nuevoEstado; }
    public void setNuevoEstado(EstadoDevolucion nuevoEstado) { this.nuevoEstado = nuevoEstado; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
