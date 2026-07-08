package com.smartlogix.inventory.repository;

import com.smartlogix.inventory.model.EstadoProducto;
import com.smartlogix.inventory.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000")})
    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.imagenes WHERE p.id = :id")
    Optional<Producto> findByIdForUpdate(@Param("id") Long id);

    // ===================== BÚSQUEDAS BÁSICAS =====================

    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.imagenes WHERE p.nombre = :nombre")
    Optional<Producto> findByNombre(@Param("nombre") String nombre);

    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.imagenes WHERE p.categoria = :categoria")
    List<Producto> findByCategoria(@Param("categoria") String categoria);

    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.imagenes WHERE p.estado = :estado")
    List<Producto> findByEstado(@Param("estado") EstadoProducto estado);

    // ===================== CONSULTAS ÚTILES INVENTORY =====================

    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.imagenes WHERE p.stock < :stock")
    List<Producto> findByStockLessThan(@Param("stock") Integer stock);

    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.imagenes WHERE p.categoria = :categoria AND p.estado = :estado")
    List<Producto> findByCategoriaAndEstado(@Param("categoria") String categoria, @Param("estado") EstadoProducto estado);

    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.imagenes WHERE (LOWER(p.nombre) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(p.marca) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(p.categoria) LIKE LOWER(CONCAT('%', :q, '%'))) AND p.estado = :estado")
    List<Producto> buscarPorTexto(@Param("q") String q, @Param("estado") EstadoProducto estado);

    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.imagenes WHERE p.oferta = true AND p.estado = :estado")
    List<Producto> findOfertasActivas(@Param("estado") EstadoProducto estado);

    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.imagenes WHERE p.nuevo = true AND p.estado = :estado")
    List<Producto> findNuevosActivos(@Param("estado") EstadoProducto estado);

    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.imagenes WHERE p.estado = :estado AND (:precioMin IS NULL OR p.precio >= :precioMin) AND (:precioMax IS NULL OR p.precio <= :precioMax) AND (:marca IS NULL OR p.marca = :marca) AND (:ratingMin IS NULL OR p.ratingPromedio >= :ratingMin)")
    List<Producto> filtrar(@Param("estado") EstadoProducto estado, @Param("precioMin") Double precioMin, @Param("precioMax") Double precioMax, @Param("marca") String marca, @Param("ratingMin") Double ratingMin);

    // ===================== CONSULTAS CON JOIN FETCH PARA EVITAR N+1 =====================

    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.imagenes")
    List<Producto> findAllWithImages();

    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.imagenes WHERE p.estado = :estado")
    List<Producto> findByEstadoWithImages(@Param("estado") EstadoProducto estado);

    // ===================== OPTIMIZACIÓN (OPCIONAL PERO PRO) =====================

    boolean existsByNombre(String nombre);

    // ===================== VENDEDOR & STATS =====================
    
    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.imagenes WHERE p.vendedorId = :vendedorId")
    List<Producto> findByVendedorId(@Param("vendedorId") String vendedorId);

    long countByStockLessThan(Integer stock);

    long countByStock(Integer stock);

    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.imagenes WHERE p.estado = :estado ORDER BY p.cantidadVendidos ASC")
    List<Producto> findTop5ByEstadoOrderByCantidadVendidosAsc(@Param("estado") EstadoProducto estado, org.springframework.data.domain.Pageable pageable);

    @Query("SELECT p FROM Producto p LEFT JOIN FETCH p.imagenes WHERE p.marca = :marca AND p.id != :id AND p.estado = 'ACTIVO'")
    List<Producto> findTop5ByMarcaAndIdNot(@Param("marca") String marca, @Param("id") Long id, org.springframework.data.domain.Pageable pageable);
}