package com.smartlogix.inventory.repository;

import com.smartlogix.inventory.model.ComentarioReporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioReporteRepository extends JpaRepository<ComentarioReporte, Long> {

    /** Verifica si un usuario ya reportó un comentario específico (evitar doble reporte) */
    boolean existsByComentarioIdAndUsuarioId(Long comentarioId, String usuarioId);

    /** Todos los reportes pendientes de un comentario */
    List<ComentarioReporte> findByComentarioIdOrderByFechaDesc(Long comentarioId);

    /** Todos los reportes no resueltos (para dashboard admin) */
    @Query("SELECT r FROM ComentarioReporte r WHERE r.resuelto = false ORDER BY r.fecha DESC")
    List<ComentarioReporte> findAllPendientes();

    /** Marcar todos los reportes de un comentario como resueltos */
    @Query("UPDATE ComentarioReporte r SET r.resuelto = true WHERE r.comentario.id = :comentarioId")
    @org.springframework.data.jpa.repository.Modifying
    void resolverPorComentario(@Param("comentarioId") Long comentarioId);
}
