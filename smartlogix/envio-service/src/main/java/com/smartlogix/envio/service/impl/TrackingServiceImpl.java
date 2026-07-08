package com.smartlogix.envio.service.impl;

import com.smartlogix.envio.dto.response.TrackingResponse;
import com.smartlogix.envio.model.Envio;
import com.smartlogix.envio.repository.EnvioRepository;
import com.smartlogix.envio.service.TrackingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TrackingServiceImpl implements TrackingService {

    private final EnvioRepository envioRepository;

    public TrackingServiceImpl(EnvioRepository envioRepository) {
        this.envioRepository = envioRepository;
    }

    @Override
    public TrackingResponse obtenerTracking(String trackingNumber) {

        Envio envio = envioRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new RuntimeException("Tracking no encontrado"));

        TrackingResponse response = new TrackingResponse();

        response.setTrackingId(envio.getTrackingNumber());
        response.setEstadoActual(envio.getEstado().name());
        response.setHistorial(new ArrayList<>());
        response.setUltimaActualizacion(envio.getFechaCreacion());

        return response;
    }
}