package com.smartlogix.inventory.repository;

import com.smartlogix.inventory.model.ProductoComentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoComentarioRepository extends JpaRepository<ProductoComentario, Long> {

    List<ProductoComentario> findByProductoIdOrderByFechaDesc(Long productoId);

    List<ProductoComentario> findByProductoIdAndActivoTrueOrderByFechaDesc(Long productoId);

    List<ProductoComentario> findByProductoIdAndUsuarioIdAndActivoTrue(Long productoId, String usuarioId);

    /** 2.2 - Prevenir doble comentario: verifica si el usuario ya tiene un comentario activo en este producto */
    boolean existsByProductoIdAndUsuarioIdAndActivoTrue(Long productoId, String usuarioId);

    // New method to enforce one comment per purchase
    boolean existsByProductoIdAndUsuarioIdAndPedidoIdAndActivoTrue(Long productoId, String usuarioId, Long pedidoId);

    /** 2.14 - Distribución de ratings por estrella para mostrar porcentajes */
    @Query("SELECT c.calificacion, COUNT(c) FROM ProductoComentario c " +
           "WHERE c.producto.id = :productoId AND c.activo = true GROUP BY c.calificacion ORDER BY c.calificacion ASC")
    List<Object[]> getDistribucionRatings(@Param("productoId") Long productoId);

    /** 2.15 - Comentarios marcados como destacados (más votados o marcados manualmente) */
    @Query("SELECT c FROM ProductoComentario c WHERE c.producto.id = :productoId " +
           "AND c.activo = true AND c.destacado = true ORDER BY c.votosUtilidad DESC")
    List<ProductoComentario> findDestacadosByProductoId(@Param("productoId") Long productoId);

    /** 2.15 - Incrementar votos de utilidad de un comentario */
    @Modifying
    @Query("UPDATE ProductoComentario c SET c.votosUtilidad = c.votosUtilidad + 1 WHERE c.id = :id")
    void incrementarVotosUtilidad(@Param("id") Long id);
}

