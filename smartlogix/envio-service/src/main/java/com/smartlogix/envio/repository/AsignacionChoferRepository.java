package com.smartlogix.envio.repository;

import com.smartlogix.envio.model.AsignacionChofer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AsignacionChoferRepository extends JpaRepository<AsignacionChofer, Long> {
    Optional<AsignacionChofer> findByEnvioId(Long envioId);
}
