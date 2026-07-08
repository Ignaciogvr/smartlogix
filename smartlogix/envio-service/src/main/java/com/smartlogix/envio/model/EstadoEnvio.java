package com.smartlogix.envio.model;

public enum EstadoEnvio {
    PENDIENTE,
    PREPARANDO,
    ASIGNADO,
    EN_RUTA,
    ENTREGADO,
    CANCELADO;

    public int level() {
        return switch (this) {
            case PENDIENTE -> 0;
            case PREPARANDO -> 1;
            case ASIGNADO -> 2;
            case EN_RUTA -> 3;
            case ENTREGADO -> 4;
            case CANCELADO -> 99;
        };
    }
}