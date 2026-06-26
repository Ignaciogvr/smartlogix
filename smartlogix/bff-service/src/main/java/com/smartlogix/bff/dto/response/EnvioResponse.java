package com.smartlogix.bff.dto.response;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.time.LocalDateTime;

public class EnvioResponse {

    @JsonAlias("id")
    private Long envioId;

    private Long pedidoId;

    @JsonAlias("trackingId")
    private String trackingCode;

    private String estado;

    private String direccionDestino;

    @JsonAlias("fechaCreacion")
    private LocalDateTime fechaEnvio;

    private LocalDateTime fechaEntregaEstimada;

    public EnvioResponse() {
    }

    public EnvioResponse(
            Long envioId,
            Long pedidoId,
            String trackingCode,
            String estado,
            String direccionDestino,
            LocalDateTime fechaEnvio,
            LocalDateTime fechaEntregaEstimada
    ) {
        this.envioId = envioId;
        this.pedidoId = pedidoId;
        this.trackingCode = trackingCode;
        this.estado = estado;
        this.direccionDestino = direccionDestino;
        this.fechaEnvio = fechaEnvio;
        this.fechaEntregaEstimada = fechaEntregaEstimada;
    }

    public Long getEnvioId() {
        return envioId;
    }

    public void setEnvioId(Long envioId) {
        this.envioId = envioId;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(
            String trackingCode
    ) {
        this.trackingCode = trackingCode;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDireccionDestino() {
        return direccionDestino;
    }

    public void setDireccionDestino(
            String direccionDestino
    ) {
        this.direccionDestino = direccionDestino;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(
            LocalDateTime fechaEnvio
    ) {
        this.fechaEnvio = fechaEnvio;
    }

    public LocalDateTime getFechaEntregaEstimada() {
        return fechaEntregaEstimada;
    }

    public void setFechaEntregaEstimada(
            LocalDateTime fechaEntregaEstimada
    ) {
        this.fechaEntregaEstimada =
                fechaEntregaEstimada;
    }
}