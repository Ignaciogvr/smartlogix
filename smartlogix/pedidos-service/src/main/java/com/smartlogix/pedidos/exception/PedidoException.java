package com.smartlogix.pedidos.exception;

public class PedidoException extends RuntimeException {

    private final String code;

    public PedidoException(String message) {
        super(message);
        this.code = "PEDIDO_ERROR";
    }

    public PedidoException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}