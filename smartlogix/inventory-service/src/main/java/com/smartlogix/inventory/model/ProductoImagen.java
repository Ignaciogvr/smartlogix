package com.smartlogix.inventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "producto_imagenes")
public class ProductoImagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "imagen_url", nullable = false, length = 500)
    private String url;

    @Column(name = "es_principal")
    private Boolean esPrincipal = false;

    private Integer orden = 0;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    public ProductoImagen() {}

    public ProductoImagen(String url, Boolean esPrincipal, Integer orden) {
        this.url = url;
        this.esPrincipal = esPrincipal != null ? esPrincipal : false;
        this.orden = orden != null ? orden : 0;
        this.fechaCreacion = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public Boolean getEsPrincipal() { return esPrincipal; }
    public void setEsPrincipal(Boolean esPrincipal) { this.esPrincipal = esPrincipal; }
    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}
