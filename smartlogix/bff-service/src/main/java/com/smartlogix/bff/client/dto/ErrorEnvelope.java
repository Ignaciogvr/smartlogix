package com.smartlogix.bff.client.dto;

public class ErrorEnvelope {
    private String status;
    private String message;

    public ErrorEnvelope() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
