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
    private final com.smartlogix.bff.client.InventoryClient inventoryClient;

    public PedidoBffServiceImpl(
            PedidoClient pedidoClient,
            PedidoAggregator pedidoAggregator,
            com.smartlogix.bff.client.InventoryClient inventoryClient
    ) {
        this.pedidoClient = pedidoClient;
        this.pedidoAggregator = pedidoAggregator;
        this.inventoryClient = inventoryClient;
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

        if (request == null) {
            throw new IllegalArgumentException("Request no puede ser null");
        }

        if (request.getProductos() == null || request.getProductos().isEmpty()) {
            throw new IllegalArgumentException("Debe incluir al menos un producto");
        }

        if (request.getUsuarioId() == null || request.getUsuarioId().trim().isEmpty()) {
            throw new IllegalArgumentException("Usuario es requerido");
        }

        if (request.getDireccionEnvio() == null || request.getDireccionEnvio().trim().isEmpty()) {
            throw new IllegalArgumentException("Dirección de envío es requerida");
        }

        CompraRequest compra = new CompraRequest();
        compra.setUsuarioId(request.getUsuarioId());
        compra.setDireccionDestino(request.getDireccionEnvio());

        // Validate products and assign vendedorId
        List<com.smartlogix.bff.dto.request.DetalleCompraRequest> validados = new java.util.ArrayList<>();
        for (com.smartlogix.bff.dto.request.DetalleCompraRequest det : request.getProductos()) {
            if (det == null) {
                throw new IllegalArgumentException("Detalle de producto no puede ser null");
            }

            if (det.getProductoId() == null) {
                throw new IllegalArgumentException("productoId es requerido");
            }

            if (det.getCantidad() == null || det.getCantidad() <= 0) {
                throw new IllegalArgumentException("cantidad debe ser mayor a 0");
            }

            com.smartlogix.bff.client.dto.ServiceEnvelope<com.smartlogix.bff.dto.response.ProductoCatalogoDTO> env = 
                inventoryClient.obtenerProducto(det.getProductoId());
            
            if (env == null || env.getData() == null) {
                throw new RuntimeException("Producto no encontrado: " + det.getProductoId());
            }
            
            com.smartlogix.bff.dto.response.ProductoCatalogoDTO prod = env.getData();
            if (prod.getStock() == null || prod.getStock() < det.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + prod.getNombre());
            }

            // Set true price and vendor ID securely from inventory
            det.setPrecioUnitario(prod.getPrecio());
            det.setVendedorId(prod.getVendedorId());
            validados.add(det);
        }
        compra.setProductos(validados);

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
