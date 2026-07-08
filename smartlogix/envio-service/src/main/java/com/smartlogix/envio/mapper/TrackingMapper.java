package com.smartlogix.envio.mapper;

import com.smartlogix.envio.dto.response.TrackingResponse;
import com.smartlogix.envio.model.Envio;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TrackingMapper {

    public TrackingResponse toResponse(Envio envio) {

        TrackingResponse response = new TrackingResponse();

        response.setTrackingId(envio.getTrackingNumber());
        response.setEstadoActual(envio.getEstado().name());
        response.setHistorial(List.of());
        response.setUltimaActualizacion(LocalDateTime.now());

        return response;
    }
}