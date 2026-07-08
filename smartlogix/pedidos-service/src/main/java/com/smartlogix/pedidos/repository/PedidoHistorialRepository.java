package com.smartlogix.pedidos.repository;

import com.smartlogix.pedidos.model.PedidoHistorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoHistorialRepository extends JpaRepository<PedidoHistorial, Long> {
    List<PedidoHistorial> findByPedidoIdOrderByFechaDesc(Long pedidoId);
}
