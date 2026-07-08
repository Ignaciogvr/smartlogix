package com.smartlogix.bff.service;

import com.smartlogix.bff.client.EnvioClient;
import com.smartlogix.bff.client.InventoryClient;
import com.smartlogix.bff.client.PedidoClient;
import com.smartlogix.bff.client.dto.ServiceEnvelope;
import com.smartlogix.bff.dto.request.ActualizarEstadoEnvioRequest;
import com.smartlogix.bff.dto.request.ProductoCreateRequest;
import com.smartlogix.bff.dto.request.ProductoUpdateRequest;
import com.smartlogix.bff.dto.response.EnvioResponse;
import com.smartlogix.bff.dto.response.PedidoResponse;
import com.smartlogix.bff.dto.response.ProductoCatalogoDTO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio BFF para operaciones administrativas.
 * Orquesta llamadas a microservicios de pedidos, envíos e inventario.
 */
@Service
public class AdminBffService {

    private final PedidoClient pedidoClient;
    private final EnvioClient envioClient;
    private final InventoryClient inventoryClient;

    public AdminBffService(PedidoClient pedidoClient, EnvioClient envioClient, InventoryClient inventoryClient) {
        this.pedidoClient = pedidoClient;
        this.envioClient = envioClient;
        this.inventoryClient = inventoryClient;
    }

    // =========================
    // DASHBOARD
    // =========================

    public java.util.Map<String, Object> obtenerDashboard() {
        // Obtener métricas básicas
        List<PedidoResponse> pedidos = pedidoClient.listarTodos();
        List<EnvioResponse> envios = envioClient.listarTodos();
        
        long pedidosPendientes = pedidos.stream()
            .filter(p -> "PENDIENTE".equals(p.getEstado()))
            .count();
        
        long pedidosEnPreparacion = pedidos.stream()
            .filter(p -> "EN_PREPARACION".equals(p.getEstado()))
            .count();

        long enviosPendientes = envios.stream()
            .filter(e -> "PENDIENTE".equals(e.getEstado()))
            .count();

        return java.util.Map.of(
            "totalPedidos", pedidos.size(),
            "pedidosPendientes", pedidosPendientes,
            "pedidosEnPreparacion", pedidosEnPreparacion,
            "totalEnvios", envios.size(),
            "enviosPendientes", enviosPendientes
        );
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

    // =========================
    // PRODUCTOS (ADMIN CRUD)
    // =========================

    public List<ProductoCatalogoDTO> listarProductos() {
        ServiceEnvelope<List<ProductoCatalogoDTO>> envelope = inventoryClient.listarProductos();
        return envelope != null && envelope.getData() != null ? envelope.getData() : List.of();
    }

    public ProductoCatalogoDTO crearProducto(ProductoCreateRequest request) {
        ServiceEnvelope<ProductoCatalogoDTO> envelope = inventoryClient.crearProducto(request);
        return envelope != null ? envelope.getData() : null;
    }

    public ProductoCatalogoDTO actualizarProducto(Long id, ProductoUpdateRequest request) {
        ServiceEnvelope<ProductoCatalogoDTO> envelope = inventoryClient.actualizarProducto(id, request);
        return envelope != null ? envelope.getData() : null;
    }

    public void eliminarProducto(Long id) {
        inventoryClient.eliminarProducto(id);
    }
}
