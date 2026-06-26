package com.smartlogix.bff.mapper;

import com.smartlogix.bff.dto.response.EnvioResponse;
import com.smartlogix.bff.dto.response.TrackingResponse;

import java.util.Collections;

public final class TrackingMapper {

    private TrackingMapper() {
    }

    public static TrackingResponse fromEnvio(EnvioResponse envio) {
        if (envio == null) {
            return null;
        }
        TrackingResponse t = new TrackingResponse();
        t.setTrackingId(envio.getTrackingCode());
        t.setEstadoActual(envio.getEstado());
        t.setHistorial(Collections.emptyList());
        t.setUltimaActualizacion(envio.getFechaEnvio());
        return t;
    }
}
