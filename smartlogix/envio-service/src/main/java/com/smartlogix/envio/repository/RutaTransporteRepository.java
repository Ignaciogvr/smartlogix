package com.smartlogix.envio.repository;

import com.smartlogix.envio.model.RutaTransporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutaTransporteRepository extends JpaRepository<RutaTransporte, Long> {
    List<RutaTransporte> findByTransportistaId(Long transportistaId);
}
