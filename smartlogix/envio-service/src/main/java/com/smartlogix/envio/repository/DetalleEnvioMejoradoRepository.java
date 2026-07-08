package com.smartlogix.envio.repository;

import com.smartlogix.envio.model.DetalleEnvioMejorado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DetalleEnvioMejoradoRepository extends JpaRepository<DetalleEnvioMejorado, Long> {
    Optional<DetalleEnvioMejorado> findByEnvioId(Long envioId);
}
