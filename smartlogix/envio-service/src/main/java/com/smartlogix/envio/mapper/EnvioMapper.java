package com.smartlogix.envio.mapper;

import com.smartlogix.envio.dto.response.EnvioResponse;
import com.smartlogix.envio.model.Envio;
import org.springframework.stereotype.Component;

@Component
public class EnvioMapper {

    public EnvioResponse toResponse(Envio envio) {

        if (envio == null) return null;

        EnvioResponse response = new EnvioResponse();

        response.setId(envio.getId());
        response.setPedidoId(envio.getPedidoId());
        response.setTrackingId(envio.getTrackingNumber());
        response.setEstado(envio.getEstado().name());
        response.setDireccionDestino(envio.getDireccionDestino());
        response.setFechaCreacion(envio.getFechaCreacion());

        return response;
    }
}