package com.smartlogix.pedidos.repository;

import com.smartlogix.pedidos.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
}