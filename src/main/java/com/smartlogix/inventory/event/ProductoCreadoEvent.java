package com.smartlogix.inventory.event;

import java.util.List;

public class ProductoCreadoEvent {

    private Long id;
    private String nombre;
    private String categoria;
    private Double precio;
    private Integer stock;
    private List<String> imagenes;

    public ProductoCreadoEvent() {
    }

    public ProductoCreadoEvent(Long id, String nombre, String categoria,
                               Double precio, Integer stock, List<String> imagenes) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.imagenes = imagenes;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public Double getPrecio() { return precio; }
    public Integer getStock() { return stock; }
    public List<String> getImagenes() { return imagenes; }

    public void setId(Long id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public void setStock(Integer stock) { this.stock = stock; }
    public void setImagenes(List<String> imagenes) { this.imagenes = imagenes; }
}