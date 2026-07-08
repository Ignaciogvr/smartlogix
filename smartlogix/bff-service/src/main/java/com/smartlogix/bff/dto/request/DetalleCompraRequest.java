package com.smartlogix.bff.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;

public class DetalleCompraRequest {

    private Long productoId;

    private Integer cantidad;

    @JsonAlias("precio")
    private Double precioUnitario;

    public DetalleCompraRequest() {}

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    private String vendedorId;

    public String getVendedorId() { return vendedorId; }
    public void setVendedorId(String vendedorId) { this.vendedorId = vendedorId; }
}