package com.smartlogix.bff.service;

import com.smartlogix.bff.dto.request.ActualizarEstadoEnvioRequest;
import com.smartlogix.bff.dto.request.AsignarChoferRequest;
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

    /**
     * Retorna todos los envíos asociados a un pedido.
     * Permite al cliente ver el seguimiento desde Mis Pedidos sin ingresar código manual.
     */
    List<EnvioResponse> enviosPorPedido(Long pedidoId);

    /**
     * Asigna un chofer a un envío pendiente.
     * Usado por ADMIN para asignar manualmente un chofer de la flota interna.
     */
    EnvioResponse asignarChofer(Long envioId, AsignarChoferRequest request);
    
    /**
     * Obtiene tracking por ID de envío (alias de obtenerTracking pero por ID numérico)
     */
    TrackingResponse obtenerTrackingPorId(Long envioId);
    
    /**
     * Cotiza un envío según región y peso
     */
    Object cotizarEnvio(String region, Double peso);
}