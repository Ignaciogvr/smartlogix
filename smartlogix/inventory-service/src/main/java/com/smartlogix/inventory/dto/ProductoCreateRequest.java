package com.smartlogix.inventory.dto;


import java.util.List;


public class ProductoCreateRequest {


    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer stock;
    private String categoria;
    private List<String> imagenes;


    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }


    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }


    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }


    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }


    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }


    public List<String> getImagenes() { return imagenes; }
    public void setImagenes(List<String> imagenes) { this.imagenes = imagenes; }
}
