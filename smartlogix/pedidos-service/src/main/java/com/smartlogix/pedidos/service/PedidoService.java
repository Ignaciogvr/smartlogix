package com.smartlogix.pedidos.service;

import com.smartlogix.pedidos.dto.PedidoRequestDTO;
import com.smartlogix.pedidos.model.Pedido;

import java.util.List;

public interface PedidoService {

    // ================= CRUD CLIENTE =================

    Pedido crearDesdeRequest(
            PedidoRequestDTO dto
    );

    Pedido obtener(Long id);

    Pedido actualizarDesdeRequest(
            Long id,
            PedidoRequestDTO dto
    );

    void eliminar(Long id);

    // ================= CLIENTE =================

    List<Pedido> porUsuario(
            String usuarioId
    );

    Pedido pagar(Long id);

    Pedido cancelar(Long id);

    Pedido reactivar(Long id);
}