package com.smartlogix.pedidos.event;

public class ProductoEvent {

    private Long productoId;
    private Integer cantidad;
    private String vendedorId;

    public ProductoEvent() {}

    public ProductoEvent(Long productoId, Integer cantidad, String vendedorId) {
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.vendedorId = vendedorId;
    }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public String getVendedorId() { return vendedorId; }
    public void setVendedorId(String vendedorId) { this.vendedorId = vendedorId; }
}
