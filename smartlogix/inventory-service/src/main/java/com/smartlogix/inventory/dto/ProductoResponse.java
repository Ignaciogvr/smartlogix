package com.smartlogix.inventory.dto;


import java.util.List;


public class ProductoResponse {


    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private String categoria;
    private List<String> imagenes;
    private Double rating;
    private Integer vendidos;


    public ProductoResponse(Long id, String nombre, String descripcion,
                            Double precio, Integer stock, String categoria,
                            List<String> imagenes, Double rating, Integer vendidos) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
        this.imagenes = imagenes;
        this.rating = rating;
        this.vendidos = vendidos;
    }


    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public Double getPrecio() { return precio; }
    public Integer getStock() { return stock; }
    public String getCategoria() { return categoria; }
    public List<String> getImagenes() { return imagenes; }
    public Double getRating() { return rating; }
    public Integer getVendidos() { return vendidos; }
}
