package com.smartlogix.bff.service;

import com.smartlogix.bff.client.InventoryClient;
import com.smartlogix.bff.client.PedidoClient;
import com.smartlogix.bff.client.UsuarioClient;
import com.smartlogix.bff.dto.CompraRequest;
import org.springframework.stereotype.Service;

@Service
public class BffService {

    private final InventoryClient inventoryClient;
    private final PedidoClient pedidoClient;
    private final UsuarioClient usuarioClient;

    public BffService(
            InventoryClient inventoryClient,
            PedidoClient pedidoClient,
            UsuarioClient usuarioClient
    ) {
        this.inventoryClient = inventoryClient;
        this.pedidoClient = pedidoClient;
        this.usuarioClient = usuarioClient;
    }

    // =========================
    // 🔥 INVENTORY
    // =========================

    public Object productos() {
        return inventoryClient.listarProductos();
    }

    public Object producto(Long id) {
        return inventoryClient.obtenerProducto(id);
    }

    public Object productosActivos() {
        return inventoryClient.productosActivos();
    }

    // =========================
    // 🔥 PEDIDOS
    // =========================

    public Object crearPedido(CompraRequest request) {
        return pedidoClient.crearPedido(request);
    }

    public Object pedidos() {
        return pedidoClient.listarPedidos();
    }

    // =========================
    // 🔥 USUARIOS
    // =========================

    public Object usuarios() {
        return usuarioClient.listarUsuarios();
    }

    public Object usuario(String auth0Id) {
        return usuarioClient.obtenerUsuario(auth0Id);
    }
}