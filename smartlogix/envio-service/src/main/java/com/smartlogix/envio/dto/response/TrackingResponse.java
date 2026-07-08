package com.smartlogix.envio.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class TrackingResponse {

    private String trackingId;
    private String estadoActual;
    private List<TimelineResponse> historial;
    private LocalDateTime ultimaActualizacion;

    public TrackingResponse() {}

    public TrackingResponse(String trackingId, String estadoActual, List<TimelineResponse> historial) {
        this.trackingId = trackingId;
        this.estadoActual = estadoActual;
        this.historial = historial;
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(String estadoActual) {
        this.estadoActual = estadoActual;
    }

    public List<TimelineResponse> getHistorial() {
        return historial;
    }

    public void setHistorial(List<TimelineResponse> historial) {
        this.historial = historial;
    }

    public LocalDateTime getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }
}