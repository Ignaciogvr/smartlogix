package com.smartlogix.inventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "productos", indexes = {
    @Index(name = "idx_productos_nombre", columnList = "nombre"),
    @Index(name = "idx_productos_categoria", columnList = "categoria"),
    @Index(name = "idx_productos_estado", columnList = "estado"),
    @Index(name = "idx_productos_precio", columnList = "precio"),
    @Index(name = "idx_productos_stock", columnList = "stock"),
    @Index(name = "idx_productos_categoria_estado", columnList = "categoria, estado")
})
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name = "vendedor_id", nullable = false)
    private String vendedorId = "default_vendor";

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(length = 255)
    private String descripcionCorta;

    @Column(length = 2000)
    private String descripcion;

    @Column(name = "precio_anterior")
    private Double precioAnterior;

    @Column(name = "descuento_porcentaje")
    private Integer descuentoPorcentaje;

    private String marca;
    private String modelo;
    private String fabricante;
    private String sku;
    private String garantia;
    private String peso;
    private String dimensiones;
    private String material;
    private String color;
    
    @Column(name = "pais_fabricacion")
    private String paisFabricacion;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private String categoria;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orden ASC")
    private List<ProductoImagen> imagenes = new ArrayList<>();

    @Column(nullable = false)
    private Double ratingPromedio = 0.0;

    @Column(nullable = false)
    private Integer totalRatings = 0;

    @Column(nullable = false)
    private Integer cantidadVendidos = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoProducto estado = EstadoProducto.ACTIVO;

    @Column(nullable = false)
    private Boolean destacado = false;

    @Column(nullable = false)
    private Boolean oferta = false;

    @Column(nullable = false)
    private Boolean nuevo = false;

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    @Column(name = "fecha_ultima_venta")
    private LocalDateTime fechaUltimaVenta;

    // ===================== CONSTRUCTORES =====================

    public Producto() {}

    public Producto(String nombre, String descripcion, Double precio,
                    Integer stock, String categoria, List<ProductoImagen> imagenes) {

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.imagenes = (imagenes != null) ? imagenes : new ArrayList<>();
        this.estado = EstadoProducto.ACTIVO;
        this.destacado = false;
        this.oferta = false;
        this.nuevo = false;
    }

    // ===================== LIFECYCLE =====================

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();

        if (this.stock != null && this.stock == 0) {
            this.estado = EstadoProducto.INACTIVO;
        }
    }

    // ===================== MÉTODOS DE NEGOCIO =====================

    public void descontarStock(int cantidad) {

        if (cantidad <= 0) {
            throw new RuntimeException("Cantidad inválida");
        }

        if (this.stock < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        this.stock -= cantidad;
        this.cantidadVendidos += cantidad;
        this.fechaUltimaVenta = LocalDateTime.now();

        if (this.stock == 0) {
            this.estado = EstadoProducto.INACTIVO;
        }
    }

    public void reponerStock(int cantidad) {

        if (cantidad <= 0) {
            throw new RuntimeException("Cantidad inválida");
        }

        this.stock += cantidad;
        this.cantidadVendidos = Math.max(0, this.cantidadVendidos - cantidad);

        if (this.stock > 0) {
            this.estado = EstadoProducto.ACTIVO;
        }
    }

    public void agregarRating(Double rating) {

        double total = this.ratingPromedio * this.totalRatings;
        total += rating;

        this.totalRatings++;
        this.ratingPromedio = total / this.totalRatings;
    }

    public void recalcularRating(List<ProductoComentario> comentarios) {
        if (comentarios == null || comentarios.isEmpty()) {
            this.ratingPromedio = 0.0;
            this.totalRatings = 0;
            return;
        }
        
        double suma = 0.0;
        for (ProductoComentario c : comentarios) {
            suma += c.getCalificacion();
        }
        this.totalRatings = comentarios.size();
        this.ratingPromedio = suma / this.totalRatings;
    }

    public void reactivar() {
        this.estado = EstadoProducto.ACTIVO;
    }

    // ===================== GETTERS =====================

    public Long getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public Integer getStock() {
        return stock;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescripcionCorta() {
        return descripcionCorta;
    }

    public Double getPrecioAnterior() {
        return precioAnterior;
    }

    public Integer getDescuentoPorcentaje() {
        return descuentoPorcentaje;
    }
    public String getVendedorId() { return vendedorId; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public String getFabricante() { return fabricante; }
    public String getSku() { return sku; }
    public String getGarantia() { return garantia; }
    public String getPeso() { return peso; }
    public String getDimensiones() { return dimensiones; }
    public String getMaterial() { return material; }
    public String getColor() { return color; }
    public String getPaisFabricacion() { return paisFabricacion; }
    
    public Integer getTotalRatings() {
        return totalRatings;
    }

    public List<ProductoImagen> getImagenes() {
        return imagenes;
    }

    public Double getRatingPromedio() {
        return ratingPromedio;
    }

    public Integer getCantidadVendidos() {
        return cantidadVendidos;
    }

    public EstadoProducto getEstado() {
        return estado;
    }

    public Boolean getDestacado() {
        return destacado;
    }

    public Boolean getOferta() {
        return oferta;
    }

    public Boolean getNuevo() {
        return nuevo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public LocalDateTime getFechaUltimaVenta() {
        return fechaUltimaVenta;
    }

    // ===================== SETTERS =====================

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setDescripcionCorta(String descripcionCorta) {
        this.descripcionCorta = descripcionCorta;
    }

    public void setPrecioAnterior(Double precioAnterior) {
        this.precioAnterior = precioAnterior;
    }

    public void setDescuentoPorcentaje(Integer descuentoPorcentaje) {
        this.descuentoPorcentaje = descuentoPorcentaje;
    }

    public void setVendedorId(String vendedorId) { this.vendedorId = vendedorId; }
    public void setMarca(String marca) { this.marca = marca; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }
    public void setSku(String sku) { this.sku = sku; }
    public void setGarantia(String garantia) { this.garantia = garantia; }
    public void setPeso(String peso) { this.peso = peso; }
    public void setDimensiones(String dimensiones) { this.dimensiones = dimensiones; }
    public void setMaterial(String material) { this.material = material; }
    public void setColor(String color) { this.color = color; }
    public void setPaisFabricacion(String paisFabricacion) { this.paisFabricacion = paisFabricacion; }
    
    public void setTotalRatings(Integer totalRatings) {
        this.totalRatings = totalRatings;
    }

    public void setImagenes(List<ProductoImagen> imagenes) {
        this.imagenes = imagenes;
    }

    public void setEstado(EstadoProducto estado) {
        this.estado = estado;
    }

    public void setDestacado(Boolean destacado) {
        this.destacado = destacado;
    }

    public void setOferta(Boolean oferta) {
        this.oferta = oferta;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
    }

    public void setFechaUltimaVenta(LocalDateTime fechaUltimaVenta) {
        this.fechaUltimaVenta = fechaUltimaVenta;
    }
}
