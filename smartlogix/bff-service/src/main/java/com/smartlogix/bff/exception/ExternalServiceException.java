package com.smartlogix.bff.exception;

public class ExternalServiceException extends RuntimeException {

    public ExternalServiceException(String service, String message) {
        super("Error en servicio [" + service + "]: " + message);
    }
}