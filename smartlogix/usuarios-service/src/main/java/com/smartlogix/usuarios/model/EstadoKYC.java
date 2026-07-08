package com.smartlogix.usuarios.model;

/**
 * Enum para el estado de verificación Know Your Customer (KYC)
 * Cumplimiento normativo: verificación de identidad y datos del usuario
 */
public enum EstadoKYC {
    PENDIENTE("Verificación pendiente"),
    VERIFICADO("Identidad verificada"),
    RECHAZADO("Verificación rechazada");

    private final String descripcion;

    EstadoKYC(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
