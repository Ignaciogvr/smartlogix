package com.smartlogix.envio.exception;

public class EnvioException extends RuntimeException {

    public EnvioException(String message) {
        super(message);
    }

    public EnvioException(String message, Throwable cause) {
        super(message, cause);
    }
}