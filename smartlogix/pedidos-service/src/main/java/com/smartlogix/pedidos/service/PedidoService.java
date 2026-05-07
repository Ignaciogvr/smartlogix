package com.smartlogix.pedidos.service;

import com.smartlogix.pedidos.model.Pedido;
import com.smartlogix.pedidos.dto.PedidoRequestDTO;

import java.util.List;

public interface PedidoService {

    List<Pedido> listar();

    Pedido crearDesdeRequest(PedidoRequestDTO dto);

    Pedido obtener(Long id);

    Pedido actualizarDesdeRequest(Long id, PedidoRequestDTO dto);

    Pedido cerrar(Long id);

    void eliminar(Long id);

    List<Pedido> porUsuario(String usuarioId);
}