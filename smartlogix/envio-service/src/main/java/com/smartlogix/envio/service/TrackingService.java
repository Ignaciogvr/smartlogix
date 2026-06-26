package com.smartlogix.envio.service;

import com.smartlogix.envio.dto.response.TrackingResponse;

public interface TrackingService {

    TrackingResponse obtenerTracking(String trackingNumber);
}