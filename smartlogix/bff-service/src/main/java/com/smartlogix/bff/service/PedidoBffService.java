package com.smartlogix.bff.service;

import com.smartlogix.bff.dto.request.CheckoutRequest;
import com.smartlogix.bff.dto.request.CompraRequest;
import com.smartlogix.bff.dto.response.EstadoCompletoResponse;
import com.smartlogix.bff.dto.response.PedidoResponse;

import java.util.List;

public interface PedidoBffService {

    // =========================
    // PEDIDOS
    // =========================

    EstadoCompletoResponse obtenerEstadoCompleto(Long pedidoId);

    PedidoResponse crearPedido(CompraRequest request);

    PedidoResponse checkout(CheckoutRequest request);

    List<PedidoResponse> listarPedidos();

    PedidoResponse obtenerPedido(Long pedidoId);

    List<PedidoResponse> pedidosUsuario(String usuarioId);

    PedidoResponse pagarPedido(Long pedidoId);

    void cancelarPedido(Long pedidoId);

    PedidoResponse reactivarPedido(Long pedidoId);

    PedidoResponse prepararPedido(Long pedidoId);

    PedidoResponse enviarPedido(Long pedidoId);

    PedidoResponse entregarPedido(Long pedidoId);
}