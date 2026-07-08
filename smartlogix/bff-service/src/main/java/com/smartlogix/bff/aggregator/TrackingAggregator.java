package com.smartlogix.bff.aggregator;

import com.smartlogix.bff.client.EnvioClient;
import com.smartlogix.bff.dto.response.EnvioResponse;
import com.smartlogix.bff.dto.response.TrackingResponse;
import com.smartlogix.bff.mapper.TrackingMapper;
import org.springframework.stereotype.Component;

@Component
public class TrackingAggregator {

    private final EnvioClient envioClient;

    public TrackingAggregator(EnvioClient envioClient) {
        this.envioClient = envioClient;
    }

    public TrackingResponse tracking(String trackingCode) {
        return envioClient.obtenerTracking(trackingCode);
    }
}