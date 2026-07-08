package com.smartlogix.envio.dto.external;

import java.util.List;

public class TrackingExternalResponse {

    private String trackingNumber;
    private String currentStatus;
    private List<String> events;

    public TrackingExternalResponse() {}

    public TrackingExternalResponse(String trackingNumber, String currentStatus, List<String> events) {
        this.trackingNumber = trackingNumber;
        this.currentStatus = currentStatus;
        this.events = events;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }
}