package com.smartlogix.inventory.repository;

import com.smartlogix.inventory.model.HistorialNavegacion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistorialNavegacionRepository extends JpaRepository<HistorialNavegacion, Long> {

    @Query("SELECT h FROM HistorialNavegacion h WHERE h.usuarioId = :usuarioId ORDER BY h.fechaVista DESC")
    List<HistorialNavegacion> findByUsuarioIdOrderByFechaVistaDesc(@Param("usuarioId") String usuarioId, Pageable pageable);

    void deleteByUsuarioIdAndProductoId(String usuarioId, Long productoId);
}
