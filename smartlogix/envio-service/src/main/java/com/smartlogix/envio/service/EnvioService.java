package com.smartlogix.envio.service;

import com.smartlogix.envio.dto.request.CrearEnvioRequest;
import com.smartlogix.envio.dto.response.EnvioResponse;

import java.util.List;

public interface EnvioService {

    EnvioResponse crearEnvio(
            CrearEnvioRequest request
    );

    // NUEVO
    EnvioResponse obtenerPorId(
            Long id
    );

    EnvioResponse obtenerPorTracking(
            String trackingNumber
    );

    List<EnvioResponse> obtenerPorPedido(
            Long pedidoId
    );

    List<EnvioResponse> listarTodos();

    List<EnvioResponse> listarPorUsuario(String usuarioId);

    EnvioResponse actualizarEstado(
            Long envioId,
            String nuevoEstado
    );

    void cancelarPorPedido(Long pedidoId);
}