package com.smartlogix.bff.service.impl;

import com.smartlogix.bff.aggregator.PedidoAggregator;
import com.smartlogix.bff.client.PedidoClient;
import com.smartlogix.bff.dto.request.CheckoutRequest;
import com.smartlogix.bff.dto.request.CompraRequest;
import com.smartlogix.bff.dto.response.PedidoResponse;
import com.smartlogix.bff.dto.response.EstadoCompletoResponse;
import com.smartlogix.bff.service.PedidoBffService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoBffServiceImpl
        implements PedidoBffService {

    private static final Logger log = LoggerFactory.getLogger(PedidoBffServiceImpl.class);

    private final PedidoClient pedidoClient;
    private final PedidoAggregator pedidoAggregator;

    public PedidoBffServiceImpl(
            PedidoClient pedidoClient,
            PedidoAggregator pedidoAggregator
    ) {
        this.pedidoClient = pedidoClient;
        this.pedidoAggregator = pedidoAggregator;
    }

    // =========================
    // ESTADO COMPLETO
    // =========================

    @Override
    public EstadoCompletoResponse obtenerEstadoCompleto(Long pedidoId) {
        return pedidoAggregator.obtenerEstadoCompleto(pedidoId);
    }

    // =========================
    // CREAR PEDIDO
    // =========================

    @Override
    public PedidoResponse crearPedido(CompraRequest request) {
        return pedidoClient.crearPedido(request);
    }

    // =========================
    // CHECKOUT
    // =========================

    @Override
    public PedidoResponse checkout(CheckoutRequest request) {

        CompraRequest compra = new CompraRequest();

        compra.setUsuarioId(request.getUsuarioId());
        compra.setProductos(request.getProductos());
        compra.setDireccionDestino(request.getDireccionEnvio());

        return pedidoClient.crearPedido(compra);
    }

    // =========================
    // LISTAR
    // =========================

    @Override
    public List<PedidoResponse> listarPedidos() {
        return pedidoClient.listarPedidos();
    }

    // =========================
    // OBTENER
    // =========================

    @Override
    public PedidoResponse obtenerPedido(Long pedidoId) {
        return pedidoClient.obtenerPedido(pedidoId);
    }

    // =========================
    // POR USUARIO
    // =========================

    @Override
    public List<PedidoResponse> pedidosUsuario(String usuarioId) {
        return pedidoClient.pedidosUsuario(usuarioId);
    }

    // =========================
    // PAGAR
    // =========================

    @Override
    public PedidoResponse pagarPedido(Long pedidoId) {
        return pedidoClient.pagarPedido(pedidoId);
    }

    // =========================
    // CANCELAR
    // =========================

    @Override
    public void cancelarPedido(Long pedidoId) {
        pedidoClient.cancelarPedido(pedidoId);
    }

    // =========================
    // REACTIVAR
    // =========================

    @Override
    public PedidoResponse reactivarPedido(Long pedidoId) {
        return pedidoClient.reactivarPedido(pedidoId);
    }

    // =========================
    // PREPARAR
    // =========================

    @Override
    public PedidoResponse prepararPedido(Long pedidoId) {
        return pedidoClient.prepararPedido(pedidoId);
    }

    // =========================
    // ENVIAR
    // =========================

    @Override
    public PedidoResponse enviarPedido(Long pedidoId) {
        return pedidoClient.enviarPedido(pedidoId);
    }

    // =========================
    // ENTREGAR
    // =========================

    @Override
    public PedidoResponse entregarPedido(Long pedidoId) {
        return pedidoClient.entregarPedido(pedidoId);
    }
}