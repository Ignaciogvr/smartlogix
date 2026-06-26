package com.smartlogix.envio.repository;

import com.smartlogix.envio.model.HistorialEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialEnvioRepository extends JpaRepository<HistorialEnvio, Long> {

    List<HistorialEnvio> findByEnvioId(Long envioId);
}