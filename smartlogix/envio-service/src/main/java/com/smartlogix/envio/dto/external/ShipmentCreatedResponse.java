package com.smartlogix.envio.dto.external;

import java.time.LocalDateTime;

public class ShipmentCreatedResponse {

    private String envioId;
    private String trackingNumber;
    private LocalDateTime createdAt;

    public ShipmentCreatedResponse() {}

    public ShipmentCreatedResponse(String envioId, String trackingNumber, LocalDateTime createdAt) {
        this.envioId = envioId;
        this.trackingNumber = trackingNumber;
        this.createdAt = createdAt;
    }

    public String getEnvioId() {
        return envioId;
    }

    public void setEnvioId(String envioId) {
        this.envioId = envioId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}