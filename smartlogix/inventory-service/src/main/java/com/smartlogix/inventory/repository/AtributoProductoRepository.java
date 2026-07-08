package com.smartlogix.inventory.repository;

import com.smartlogix.inventory.model.AtributoProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtributoProductoRepository extends JpaRepository<AtributoProducto, Long> {
    List<AtributoProducto> findByProductoId(Long productoId);
}
