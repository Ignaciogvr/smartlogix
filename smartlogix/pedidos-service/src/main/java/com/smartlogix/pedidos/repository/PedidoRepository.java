package com.smartlogix.pedidos.repository;

import com.smartlogix.pedidos.model.EstadoPedido;
import com.smartlogix.pedidos.model.Pedido;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository
        extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuarioId(
            String usuarioId
    );

    List<Pedido> findByEstado(
            EstadoPedido estado
    );
}