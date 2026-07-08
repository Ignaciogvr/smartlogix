package com.smartlogix.inventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Categorías de productos (en lugar de solo VARCHAR)
 */
@Entity
@Table(name = "categorias_producto", indexes = {
    @Index(name = "idx_categorias_slug", columnList = "slug"),
    @Index(name = "idx_categorias_activa", columnList = "activa")
})
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

    private String descripcion;

    @Column(unique = true)
    private String slug;

    private String imagenUrl;

    private Integer ordenVisualizacion = 0;

    @Column(nullable = false)
    private Boolean activa = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaActualizacion;

    public Categoria() {}

    public Categoria(String nombre, String slug) {
        this.nombre = nombre;
        this.slug = slug;
        this.activa = true;
        this.ordenVisualizacion = 0;
    }

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public Integer getOrdenVisualizacion() { return ordenVisualizacion; }
    public void setOrdenVisualizacion(Integer ordenVisualizacion) { this.ordenVisualizacion = ordenVisualizacion; }
    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
}
