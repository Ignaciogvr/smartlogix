package com.smartlogix.inventory.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer stock;

    private String categoria;

    private String imagenUrl;

    private Double ratingPromedio = 0.0;

    private Integer cantidadVendidos = 0;

    private String estado = "ACTIVO";

    // ⭐ opcional (para extender luego reviews reales)
    @ElementCollection
    private List<Double> ratings = new ArrayList<>();

    public Producto() {}

    public Producto(String nombre, String descripcion, Double precio, Integer stock, String categoria, String imagenUrl) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.imagenUrl = imagenUrl;
        this.estado = "ACTIVO";
        this.ratingPromedio = 0.0;
        this.cantidadVendidos = 0;
    }

    // GETTERS
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Double getPrecio() { return precio; }
    public Integer getStock() { return stock; }
    public String getCategoria() { return categoria; }
    public String getImagenUrl() { return imagenUrl; }
    public Double getRatingPromedio() { return ratingPromedio; }
    public Integer getCantidadVendidos() { return cantidadVendidos; }
    public String getEstado() { return estado; }

    // SETTERS
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public void setStock(Integer stock) { this.stock = stock; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public void setRatingPromedio(Double ratingPromedio) { this.ratingPromedio = ratingPromedio; }
    public void setCantidadVendidos(Integer cantidadVendidos) { this.cantidadVendidos = cantidadVendidos; }
    public void setEstado(String estado) { this.estado = estado; }

    // 🔥 lógica interna simple de rating
    public void agregarRating(Double rating) {
        this.ratings.add(rating);
        this.ratingPromedio = this.ratings.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);
    }
}