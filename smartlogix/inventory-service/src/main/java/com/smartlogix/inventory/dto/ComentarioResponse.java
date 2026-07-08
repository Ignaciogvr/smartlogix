package com.smartlogix.inventory.dto;

import java.time.LocalDateTime;

public class ComentarioResponse {

    private Long id;
    private String usuarioId;
    private String nombreCliente;
    private LocalDateTime fecha;
    private LocalDateTime fechaActualizacion;
    private Integer calificacion;
    private String comentario;
    private Boolean compraVerificada;
    private String respuestaEmpresa;
    private String imagenUrl;
    private Boolean destacado;
    private Integer votosUtilidad;

    public ComentarioResponse() {}

    public ComentarioResponse(
            Long id,
            String usuarioId,
            String nombreCliente,
            LocalDateTime fecha,
            LocalDateTime fechaActualizacion,
            Integer calificacion,
            String comentario,
            Boolean compraVerificada,
            String respuestaEmpresa
    ) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.nombreCliente = nombreCliente;
        this.fecha = fecha;
        this.fechaActualizacion = fechaActualizacion;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.compraVerificada = compraVerificada;
        this.respuestaEmpresa = respuestaEmpresa;
    }

    public ComentarioResponse(
            Long id,
            String usuarioId,
            String nombreCliente,
            LocalDateTime fecha,
            LocalDateTime fechaActualizacion,
            Integer calificacion,
            String comentario,
            Boolean compraVerificada,
            String respuestaEmpresa,
            String imagenUrl
    ) {
        this(id, usuarioId, nombreCliente, fecha, fechaActualizacion, calificacion, comentario, compraVerificada, respuestaEmpresa);
        this.imagenUrl = imagenUrl;
    }

    public Long getId() { return id; }
    public String getUsuarioId() { return usuarioId; }
    public String getNombreCliente() { return nombreCliente; }
    public LocalDateTime getFecha() { return fecha; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public Integer getCalificacion() { return calificacion; }
    public String getComentario() { return comentario; }
    public Boolean getCompraVerificada() { return compraVerificada; }
    public String getRespuestaEmpresa() { return respuestaEmpresa; }
    public String getImagenUrl() { return imagenUrl; }

    public void setId(Long id) { this.id = id; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public void setCompraVerificada(Boolean compraVerificada) { this.compraVerificada = compraVerificada; }
    public void setRespuestaEmpresa(String respuestaEmpresa) { this.respuestaEmpresa = respuestaEmpresa; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public Boolean getDestacado() { return destacado; }
    public void setDestacado(Boolean destacado) { this.destacado = destacado; }
    public Integer getVotosUtilidad() { return votosUtilidad; }
    public void setVotosUtilidad(Integer votosUtilidad) { this.votosUtilidad = votosUtilidad; }
}