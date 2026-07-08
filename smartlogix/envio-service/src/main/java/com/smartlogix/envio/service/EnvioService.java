package com.smartlogix.envio.service;

import com.smartlogix.envio.dto.request.CrearEnvioRequest;
import com.smartlogix.envio.dto.response.EnvioResponse;

import java.util.List;

public interface EnvioService {

    EnvioResponse crearEnvio(
            CrearEnvioRequest request
    );

    EnvioResponse crearEnvioInterno(Long pedidoId, String usuarioId, String direccionDestino);

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

    List<EnvioResponse> listarPorChofer(String choferId);

    EnvioResponse actualizarEstado(
            Long envioId,
            String nuevoEstado
    );

    EnvioResponse asignarChofer(Long envioId, String choferId, String choferNombre);

    void cancelarPorPedido(Long pedidoId);

    EnvioResponse marcarEntregado(Long id, com.smartlogix.envio.dto.request.EntregarRequest request, String choferId);
}