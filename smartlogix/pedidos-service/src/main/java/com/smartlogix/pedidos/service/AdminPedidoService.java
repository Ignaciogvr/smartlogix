package com.smartlogix.pedidos.service;

import com.smartlogix.pedidos.model.EstadoPedido;
import com.smartlogix.pedidos.model.Pedido;

import java.util.List;

public interface AdminPedidoService {

    // ================= ADMIN =================

    List<Pedido> listar();

    List<Pedido> porEstado(
            EstadoPedido estado
    );

    Pedido preparar(Long id);

    Pedido enviar(Long id);

    Pedido entregar(Long id);
}