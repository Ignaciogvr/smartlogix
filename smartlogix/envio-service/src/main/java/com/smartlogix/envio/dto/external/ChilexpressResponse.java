package com.smartlogix.envio.dto.external;

public class ChilexpressResponse {

    private String trackingNumber;
    private String status;
    private String message;

    public ChilexpressResponse() {}

    public ChilexpressResponse(String trackingNumber, String status, String message) {
        this.trackingNumber = trackingNumber;
        this.status = status;
        this.message = message;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}