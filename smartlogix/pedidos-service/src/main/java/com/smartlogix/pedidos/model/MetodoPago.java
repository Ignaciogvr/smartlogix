package com.smartlogix.pedidos.model;

public enum MetodoPago {
    TARJETA_CREDITO("Tarjeta de Crédito"),
    TARJETA_DEBITO("Tarjeta de Débito"),
    TRANSFERENCIA("Transferencia Bancaria"),
    EFECTIVO("Efectivo"),
    CUOTAS("Cuotas"),
    BILLETERA_DIGITAL("Billetera Digital");

    private final String descripcion;

    MetodoPago(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
