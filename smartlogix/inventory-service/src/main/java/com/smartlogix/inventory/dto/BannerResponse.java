package com.smartlogix.inventory.dto;

import java.time.LocalDateTime;

public class BannerResponse {
    private Long id;
    private String titulo;
    private String descripcion;
    private String imagenUrl;
    private String rutaDestino;
    private Boolean activo;
    private Integer orden;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public BannerResponse() {}

    public BannerResponse(Long id, String titulo, String descripcion, String imagenUrl, String rutaDestino, Boolean activo, Integer orden, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
        this.rutaDestino = rutaDestino;
        this.activo = activo;
        this.orden = orden;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public String getRutaDestino() { return rutaDestino; }
    public void setRutaDestino(String rutaDestino) { this.rutaDestino = rutaDestino; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
