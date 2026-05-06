package com.smartlogix.inventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 CONCURRENCIA
    @Version
    private Long version;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private String categoria;

    @ElementCollection
    @CollectionTable(name = "producto_imagenes", joinColumns = @JoinColumn(name = "producto_id"))
    @Column(name = "imagen_url")
    private List<String> imagenes = new ArrayList<>();

    @Column(nullable = false)
    private Double ratingPromedio = 0.0;

    @Column(nullable = false)
    private Integer totalRatings = 0;

    @Column(nullable = false)
    private Integer cantidadVendidos = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoProducto estado = EstadoProducto.ACTIVO;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    public Producto() {}

    public Producto(String nombre, String descripcion, Double precio,
                    Integer stock, String categoria, List<String> imagenes) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.imagenes = imagenes;
    }

    @PrePersist
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();

        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }

        if (this.stock == 0) {
            this.estado = EstadoProducto.INACTIVO;
        }
    }

    public void agregarRating(Double rating) {
        double total = this.ratingPromedio * this.totalRatings;
        total += rating;
        this.totalRatings++;
        this.ratingPromedio = total / this.totalRatings;
    }

    // getters
    public Long getId() { return id; }
    public Long getVersion() { return version; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Double getPrecio() { return precio; }
    public Integer getStock() { return stock; }
    public String getCategoria() { return categoria; }
    public List<String> getImagenes() { return imagenes; }
    public Double getRatingPromedio() { return ratingPromedio; }
    public Integer getCantidadVendidos() { return cantidadVendidos; }
    public EstadoProducto getEstado() { return estado; }

    // setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public void setStock(Integer stock) { this.stock = stock; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setImagenes(List<String> imagenes) { this.imagenes = imagenes; }
    public void setCantidadVendidos(Integer cantidadVendidos) { this.cantidadVendidos = cantidadVendidos; }
    public void setEstado(EstadoProducto estado) { this.estado = estado; }
}