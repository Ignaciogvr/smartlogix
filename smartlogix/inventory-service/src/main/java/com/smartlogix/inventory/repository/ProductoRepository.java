package com.smartlogix.inventory.repository;

import com.smartlogix.inventory.model.EstadoProducto;
import com.smartlogix.inventory.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // ===================== BÚSQUEDAS BÁSICAS =====================

    Optional<Producto> findByNombre(String nombre);

    List<Producto> findByCategoria(String categoria);

    List<Producto> findByEstado(EstadoProducto estado);

    // ===================== CONSULTAS ÚTILES INVENTORY =====================

    List<Producto> findByStockLessThan(Integer stock);

    List<Producto> findByCategoriaAndEstado(String categoria, EstadoProducto estado);

    // ===================== OPTIMIZACIÓN (OPCIONAL PERO PRO) =====================

    boolean existsByNombre(String nombre);
}