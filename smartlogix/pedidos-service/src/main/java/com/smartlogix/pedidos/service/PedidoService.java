package com.smartlogix.pedidos.service;

import com.smartlogix.pedidos.model.Pedido;
import com.smartlogix.pedidos.dto.PedidoDTO;

import java.util.List;

public interface PedidoService {

    List<Pedido> listar();

    Pedido crearDesdeDTO(PedidoDTO dto);

    Pedido obtener(Long id);

    Pedido actualizarDesdeDTO(Long id, PedidoDTO dto);

    Pedido cerrar(Long id);

    void eliminar(Long id);

    List<Pedido> porUsuario(String usuarioId);
}