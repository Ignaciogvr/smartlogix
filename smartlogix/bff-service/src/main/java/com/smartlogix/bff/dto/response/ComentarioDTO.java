package com.smartlogix.bff.dto.response;

import java.time.LocalDateTime;

public class ComentarioDTO {
    private Long id;
    private String usuarioId;
    private String nombreCliente;
    private LocalDateTime fecha;
    private LocalDateTime fechaActualizacion;
    private Integer calificacion;
    private String comentario;
    private Boolean compraVerificada;
    private String respuestaEmpresa;

    public ComentarioDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public Boolean getCompraVerificada() { return compraVerificada; }
    public void setCompraVerificada(Boolean compraVerificada) { this.compraVerificada = compraVerificada; }

    public String getRespuestaEmpresa() { return respuestaEmpresa; }
    public void setRespuestaEmpresa(String respuestaEmpresa) { this.respuestaEmpresa = respuestaEmpresa; }
}
