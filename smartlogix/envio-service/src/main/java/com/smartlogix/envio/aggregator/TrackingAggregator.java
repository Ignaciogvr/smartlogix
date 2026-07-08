package com.smartlogix.envio.aggregator;

import com.smartlogix.envio.dto.response.TrackingResponse;
import com.smartlogix.envio.dto.response.TimelineResponse;
import com.smartlogix.envio.model.Envio;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TrackingAggregator {

    public TrackingResponse buildTracking(Envio envio, List<TimelineResponse> eventos) {

        TrackingResponse response = new TrackingResponse();

        response.setTrackingId(envio.getTrackingNumber());
        response.setEstadoActual(envio.getEstado().name());
        response.setHistorial(eventos);
        response.setUltimaActualizacion(LocalDateTime.now());

        return response;
    }

    public List<TrackingResponse> buildTrackingList(List<Envio> envios) {

        return envios.stream()
                .map(e -> {
                    TrackingResponse r = new TrackingResponse();
                    r.setTrackingId(e.getTrackingNumber());
                    r.setEstadoActual(e.getEstado().name());
                    r.setHistorial(List.of());
                    r.setUltimaActualizacion(LocalDateTime.now());
                    return r;
                })
                .collect(Collectors.toList());
    }
}