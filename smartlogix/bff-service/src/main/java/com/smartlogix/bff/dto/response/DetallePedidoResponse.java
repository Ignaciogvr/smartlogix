package com.smartlogix.bff.dto.response;

public class DetallePedidoResponse {

    private Long productoId;

    private String nombreProducto;

    private Integer cantidad;

    private Double precioUnitario;

    private Double subtotal;

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
}