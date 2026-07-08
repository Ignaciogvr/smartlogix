package com.smartlogix.envio.repository;

import com.smartlogix.envio.model.EventoTrackingDetallado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoTrackingDetalladoRepository extends JpaRepository<EventoTrackingDetallado, Long> {
    List<EventoTrackingDetallado> findByEnvioIdOrderByTimestampEventoDesc(Long envioId);
}
