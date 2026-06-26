package com.smartlogix.inventory.repository;

import com.smartlogix.inventory.model.ProductoComentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoComentarioRepository extends JpaRepository<ProductoComentario, Long> {
    List<ProductoComentario> findByProductoIdOrderByFechaDesc(Long productoId);
    List<ProductoComentario> findByProductoIdAndActivoTrueOrderByFechaDesc(Long productoId);
    List<ProductoComentario> findByProductoIdAndUsuarioIdAndActivoTrue(Long productoId, String usuarioId);
}
