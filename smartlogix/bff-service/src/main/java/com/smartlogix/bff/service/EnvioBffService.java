package com.smartlogix.bff.service;

import com.smartlogix.bff.dto.request.ActualizarEstadoEnvioRequest;
import com.smartlogix.bff.dto.request.CrearEnvioRequest;
import com.smartlogix.bff.dto.response.EnvioResponse;
import com.smartlogix.bff.dto.response.TrackingResponse;

import java.util.List;

public interface EnvioBffService {

    // =========================
    // ENVIOS
    // =========================

    EnvioResponse crearEnvio(
            CrearEnvioRequest request
    );

    List<EnvioResponse> listarEnvios();

    EnvioResponse obtenerEnvio(
            Long envioId
    );

    TrackingResponse obtenerTracking(
            String trackingCode
    );

    List<EnvioResponse> enviosUsuario(
            String usuarioId
    );

    EnvioResponse actualizarEstado(
            Long envioId,
            ActualizarEstadoEnvioRequest request
    );
}