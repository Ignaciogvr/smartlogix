package com.smartlogix.inventory.repository;

import com.smartlogix.inventory.model.PedidoProveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoProveedorRepository extends JpaRepository<PedidoProveedor, Long> {
    List<PedidoProveedor> findByProveedorId(Long proveedorId);
    Optional<PedidoProveedor> findByNumeroPedido(String numeroPedido);
}
