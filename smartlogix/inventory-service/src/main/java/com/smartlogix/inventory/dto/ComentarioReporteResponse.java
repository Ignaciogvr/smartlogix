package com.smartlogix.inventory.dto;

import java.time.LocalDateTime;

public class ComentarioReporteResponse {
    private Long id;
    private Long comentarioId;
    private String usuarioId;
    private String motivo;
    private LocalDateTime fecha;
    private Boolean resuelto;

    public ComentarioReporteResponse() {}
    public ComentarioReporteResponse(Long id, Long comentarioId, String usuarioId,
                                      String motivo, LocalDateTime fecha, Boolean resuelto) {
        this.id = id;
        this.comentarioId = comentarioId;
        this.usuarioId = usuarioId;
        this.motivo = motivo;
        this.fecha = fecha;
        this.resuelto = resuelto;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getComentarioId() { return comentarioId; }
    public void setComentarioId(Long comentarioId) { this.comentarioId = comentarioId; }
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public Boolean getResuelto() { return resuelto; }
    public void setResuelto(Boolean resuelto) { this.resuelto = resuelto; }
}
