package com.smartlogix.bff.service;

import com.smartlogix.bff.client.EnvioClient;
import com.smartlogix.bff.client.PedidoClient;
import com.smartlogix.bff.dto.request.ActualizarEstadoEnvioRequest;
import com.smartlogix.bff.dto.response.EnvioResponse;
import com.smartlogix.bff.dto.response.PedidoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio BFF para operaciones administrativas.
 * Orquesta llamadas a microservicios de pedidos y envíos.
 */
@Service
public class AdminBffService {

    private final PedidoClient pedidoClient;
    private final EnvioClient envioClient;

    public AdminBffService(PedidoClient pedidoClient, EnvioClient envioClient) {
        this.pedidoClient = pedidoClient;
        this.envioClient = envioClient;
    }

    // =========================
    // PEDIDOS
    // =========================

    public List<PedidoResponse> listarTodosPedidos() {
        return pedidoClient.listarTodos();
    }

    public List<PedidoResponse> pedidosPorEstado(String estado) {
        return pedidoClient.porEstado(estado);
    }

    public PedidoResponse prepararPedido(Long id) {
        return pedidoClient.preparar(id);
    }

    public PedidoResponse enviarPedido(Long id) {
        return pedidoClient.enviar(id);
    }

    public PedidoResponse entregarPedido(Long id) {
        return pedidoClient.entregar(id);
    }

    // =========================
    // ENVÍOS
    // =========================

    public List<EnvioResponse> listarTodosEnvios() {
        return envioClient.listarTodos();
    }

    public EnvioResponse actualizarEstadoEnvio(Long id, ActualizarEstadoEnvioRequest request) {
        return envioClient.actualizarEstado(id, request);
    }
}
