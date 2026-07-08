package com.smartlogix.pedidos.model;

public enum EstadoPago {
    PENDIENTE("Pago pendiente"),
    PROCESANDO("Procesando pago"),
    CONFIRMADO("Pago confirmado"),
    RECHAZADO("Pago rechazado"),
    REEMBOLSADO("Pago reembolsado"),
    CANCELADO("Pago cancelado");

    private final String descripcion;

    EstadoPago(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
