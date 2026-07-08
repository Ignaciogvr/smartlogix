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

    long countByEstado(EstadoPedido estado);

    @org.springframework.data.jpa.repository.Query(
        "SELECT DISTINCT p FROM Pedido p JOIN p.detalles d WHERE d.vendedorId = :vendedorId ORDER BY p.fecha DESC"
    )
    List<Pedido> findPedidosConProductosDeVendedor(@org.springframework.data.repository.query.Param("vendedorId") String vendedorId);
}