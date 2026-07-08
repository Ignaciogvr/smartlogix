package com.smartlogix.pedidos.dto;

import com.smartlogix.pedidos.model.MotivoDevolucion;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class DevolucionRequestDTO {

    @NotNull
    private Long pedidoId;

    @NotNull
    private Long detallePedidoId;

    @NotNull
    @Size(max = 100)
    private String usuarioId;

    @NotNull
    private MotivoDevolucion motivoDevolucion;

    @Size(max = 1000)
    private String descripcionProblema;

    @Positive
    private Integer cantidadDevuelta = 1;

    @Size(max = 500)
    private String fotoEvidenciaUrl;

    // Getters and Setters
    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    public Long getDetallePedidoId() { return detallePedidoId; }
    public void setDetallePedidoId(Long detallePedidoId) { this.detallePedidoId = detallePedidoId; }
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    public MotivoDevolucion getMotivoDevolucion() { return motivoDevolucion; }
    public void setMotivoDevolucion(MotivoDevolucion motivoDevolucion) { this.motivoDevolucion = motivoDevolucion; }
    public String getDescripcionProblema() { return descripcionProblema; }
    public void setDescripcionProblema(String descripcionProblema) { this.descripcionProblema = descripcionProblema; }
    public Integer getCantidadDevuelta() { return cantidadDevuelta; }
    public void setCantidadDevuelta(Integer cantidadDevuelta) { this.cantidadDevuelta = cantidadDevuelta; }
    public String getFotoEvidenciaUrl() { return fotoEvidenciaUrl; }
    public void setFotoEvidenciaUrl(String fotoEvidenciaUrl) { this.fotoEvidenciaUrl = fotoEvidenciaUrl; }
}
