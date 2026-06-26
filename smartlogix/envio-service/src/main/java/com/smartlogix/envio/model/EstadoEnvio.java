package com.smartlogix.envio.model;

public enum EstadoEnvio {
    PENDIENTE,
    PREPARANDO,
    EN_RUTA,
    ENTREGADO,
    CANCELADO;

    public int level() {
        return switch (this) {
            case PENDIENTE -> 0;
            case PREPARANDO -> 1;
            case EN_RUTA -> 2;
            case ENTREGADO -> 3;
            case CANCELADO -> 99;
        };
    }
}