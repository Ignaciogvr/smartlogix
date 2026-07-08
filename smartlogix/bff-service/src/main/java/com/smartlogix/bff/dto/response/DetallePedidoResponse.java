package com.smartlogix.bff.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DetallePedidoResponse {


    private Long productoId;

    private String nombreProducto;

    private Integer cantidad;

    private Double precioUnitario;

    private Double subtotal;

    private String vendedorId;

    public DetallePedidoResponse() {
    }

    public DetallePedidoResponse(
            Long productoId,
            String nombreProducto,
            Integer cantidad,
            Double precioUnitario,
            Double subtotal
    ) {
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
    }

    public Long getProductoId() {
        return productoId;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public String getVendedorId() { return vendedorId; }
    public void setVendedorId(String vendedorId) { this.vendedorId = vendedorId; }
}