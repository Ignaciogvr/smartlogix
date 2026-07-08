package com.smartlogix.inventory.repository;

import com.smartlogix.inventory.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    Optional<Proveedor> findByRutProveedor(String rutProveedor);
    Optional<Proveedor> findByNombreProveedor(String nombreProveedor);
}
