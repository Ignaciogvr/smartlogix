package com.smartlogix.envio.dto.response;

import java.time.LocalDateTime;

public class EnvioResponse {

    private Long id;

    private String fotoEntregaUrl;

    private String firmaReceptorUrl;
    private Long pedidoId;
    private String trackingId;
    private String estado;
    private String direccionDestino;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaEstimadaEntrega;
    private String choferId;
    private String choferNombre;

    public EnvioResponse() {}

    public Long getId() { return id; }

    public String getFotoEntregaUrl() { return fotoEntregaUrl; }

    public void setFotoEntregaUrl(String fotoEntregaUrl) { this.fotoEntregaUrl = fotoEntregaUrl; }

    public String getFirmaReceptorUrl() { return firmaReceptorUrl; }

    public void setFirmaReceptorUrl(String firmaReceptorUrl) { this.firmaReceptorUrl = firmaReceptorUrl; }
    public void setId(Long id) { this.id = id; }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public String getTrackingId() { return trackingId; }
    public void setTrackingId(String trackingId) { this.trackingId = trackingId; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFechaEstimadaEntrega() { return fechaEstimadaEntrega; }
    public void setFechaEstimadaEntrega(LocalDateTime fechaEstimadaEntrega) { this.fechaEstimadaEntrega = fechaEstimadaEntrega; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getDireccionDestino() { return direccionDestino; }
    public void setDireccionDestino(String direccionDestino) { this.direccionDestino = direccionDestino; }

    public String getChoferId() { return choferId; }
    public void setChoferId(String choferId) { this.choferId = choferId; }

    public String getChoferNombre() { return choferNombre; }
    public void setChoferNombre(String choferNombre) { this.choferNombre = choferNombre; }
}