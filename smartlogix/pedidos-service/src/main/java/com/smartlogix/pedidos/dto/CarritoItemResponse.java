package com.smartlogix.pedidos.dto;

public class CarritoItemResponse {
    private Long id;
    private Long productoId;
    private Integer cantidad;
    private Double precio;

    public CarritoItemResponse(Long id, Long productoId, Integer cantidad, Double precio) {
        this.id = id;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
}
