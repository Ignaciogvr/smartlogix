package com.smartlogix.bff.model;

import com.smartlogix.bff.dto.response.TimelineResponse;

import java.time.LocalDateTime;
import java.util.List;

public class TrackingView {

    // =========================
    // TRACKING
    // =========================

    private String trackingCode;

    private String estadoActual;

    private String ubicacionActual;

    private String transportista;

    // =========================
    // PEDIDO
    // =========================

    private Long pedidoId;

    private String usuarioId;

    // =========================
    // FECHAS
    // =========================

    private LocalDateTime fechaSalida;

    private LocalDateTime fechaEstimadaEntrega;

    private LocalDateTime fechaEntregado;

    // =========================
    // TIMELINE
    // =========================

    private List<TimelineResponse> timeline;

    // =========================
    // CONSTRUCTORES
    // =========================

    public TrackingView() {
    }

    public TrackingView(
            String trackingCode,
            String estadoActual,
            String ubicacionActual,
            String transportista,
            Long pedidoId,
            String usuarioId,
            LocalDateTime fechaSalida,
            LocalDateTime fechaEstimadaEntrega,
            LocalDateTime fechaEntregado,
            List<TimelineResponse> timeline
    ) {
        this.trackingCode = trackingCode;
        this.estadoActual = estadoActual;
        this.ubicacionActual = ubicacionActual;
        this.transportista = transportista;
        this.pedidoId = pedidoId;
        this.usuarioId = usuarioId;
        this.fechaSalida = fechaSalida;
        this.fechaEstimadaEntrega = fechaEstimadaEntrega;
        this.fechaEntregado = fechaEntregado;
        this.timeline = timeline;
    }

    // =========================
    // GETTERS & SETTERS
    // =========================

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public String getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(String estadoActual) {
        this.estadoActual = estadoActual;
    }

    public String getUbicacionActual() {
        return ubicacionActual;
    }

    public void setUbicacionActual(String ubicacionActual) {
        this.ubicacionActual = ubicacionActual;
    }

    public String getTransportista() {
        return transportista;
    }

    public void setTransportista(String transportista) {
        this.transportista = transportista;
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

    public LocalDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public LocalDateTime getFechaEstimadaEntrega() {
        return fechaEstimadaEntrega;
    }

    public void setFechaEstimadaEntrega(LocalDateTime fechaEstimadaEntrega) {
        this.fechaEstimadaEntrega = fechaEstimadaEntrega;
    }

    public LocalDateTime getFechaEntregado() {
        return fechaEntregado;
    }

    public void setFechaEntregado(LocalDateTime fechaEntregado) {
        this.fechaEntregado = fechaEntregado;
    }

    public List<TimelineResponse> getTimeline() {
        return timeline;
    }

    public void setTimeline(List<TimelineResponse> timeline) {
        this.timeline = timeline;
    }
}