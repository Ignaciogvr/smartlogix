package com.smartlogix.inventory.repository;

import com.smartlogix.inventory.model.DetallePedidoProveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoProveedorRepository extends JpaRepository<DetallePedidoProveedor, Long> {
    List<DetallePedidoProveedor> findByPedidoProveedorId(Long pedidoProveedorId);
}
