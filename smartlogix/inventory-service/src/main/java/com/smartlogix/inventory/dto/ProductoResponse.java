package com.smartlogix.inventory.dto;


import java.util.List;


public class ProductoResponse {


    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private String categoria;
    private String imagenPrincipal;
    private List<String> imagenes;
    private Double rating;
    private Integer vendidos;
    private Boolean destacado;
    private Boolean oferta;
    private Boolean nuevo;

    private String descripcionCorta;
    private Double precioAnterior;
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
    private String paisFabricacion;
    private Integer totalRatings;
    private String vendedorId;

    public ProductoResponse(Long id, String nombre, String descripcion,
                            Double precio, Integer stock, String categoria,
                            String imagenPrincipal, List<String> imagenes, Double rating, Integer vendidos,
                            Boolean destacado, Boolean oferta, Boolean nuevo,
                            String descripcionCorta, Double precioAnterior, Integer descuentoPorcentaje,
                            String marca, String modelo, String fabricante, String sku, String garantia,
                            String peso, String dimensiones, String material, String color, String paisFabricacion,
                            Integer totalRatings, String vendedorId) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.imagenPrincipal = imagenPrincipal;
        this.imagenes = imagenes;
        this.rating = rating;
        this.vendidos = vendidos;
        this.destacado = destacado;
        this.oferta = oferta;
        this.nuevo = nuevo;
        this.descripcionCorta = descripcionCorta;
        this.precioAnterior = precioAnterior;
        this.descuentoPorcentaje = descuentoPorcentaje;
        this.marca = marca;
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.sku = sku;
        this.garantia = garantia;
        this.peso = peso;
        this.dimensiones = dimensiones;
        this.material = material;
        this.color = color;
        this.paisFabricacion = paisFabricacion;
        this.totalRatings = totalRatings;
        this.vendedorId = vendedorId;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Double getPrecio() { return precio; }
    public Integer getStock() { return stock; }
    public String getCategoria() { return categoria; }
    public String getImagenPrincipal() { return imagenPrincipal; }
    public List<String> getImagenes() { return imagenes; }
    public Double getRating() { return rating; }
    public Integer getVendidos() { return vendidos; }
    public Boolean getDestacado() { return destacado; }
    public Boolean getOferta() { return oferta; }
    public Boolean getNuevo() { return nuevo; }

    public String getDescripcionCorta() { return descripcionCorta; }
    public Double getPrecioAnterior() { return precioAnterior; }
    public Integer getDescuentoPorcentaje() { return descuentoPorcentaje; }
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
    public Integer getTotalRatings() { return totalRatings; }
    public String getVendedorId() { return vendedorId; }
}
