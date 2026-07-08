package com.smartlogix.pedidos.dto;

public class CarritoItemRequest {
    private Long productoId;
    private Integer cantidad;
    private Double precio; // Usualmente viene del BFF o lo validamos contra Inventory

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
}
