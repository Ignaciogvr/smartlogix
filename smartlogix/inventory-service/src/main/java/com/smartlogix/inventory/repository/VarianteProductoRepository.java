package com.smartlogix.inventory.repository;

import com.smartlogix.inventory.model.VarianteProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VarianteProductoRepository extends JpaRepository<VarianteProducto, Long> {
    List<VarianteProducto> findByProductoId(Long productoId);
    Optional<VarianteProducto> findByCodigoSku(String codigoSku);
}
