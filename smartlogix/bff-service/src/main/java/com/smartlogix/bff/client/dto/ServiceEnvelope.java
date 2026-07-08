package com.smartlogix.bff.client.dto;

/**
 * Respuesta envolvente común de los microservicios (status/message/data numérico).
 */
public class ServiceEnvelope<T> {

    private int status;
    private String message;
    private T data;

    public ServiceEnvelope() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
