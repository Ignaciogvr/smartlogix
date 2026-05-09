package com.smartlogix.bff.dto.response;

import java.time.LocalDateTime;

public class PedidoResponse {

    private Long id;
    private String usuarioId;
    private Double total;
    private String estado;
    private LocalDateTime fecha;

    public PedidoResponse() {}

    public PedidoResponse(
            Long id,
            String usuarioId,
            Double total,
            String estado,
            LocalDateTime fecha
    ) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.total = total;
        this.estado = estado;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public Double getTotal() {
        return total;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}