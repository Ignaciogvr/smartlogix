package com.smartlogix.envio.repository;

import com.smartlogix.envio.model.ReclamoLogistica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReclamoLogisticaRepository extends JpaRepository<ReclamoLogistica, Long> {
    List<ReclamoLogistica> findByEnvioId(Long envioId);
}
