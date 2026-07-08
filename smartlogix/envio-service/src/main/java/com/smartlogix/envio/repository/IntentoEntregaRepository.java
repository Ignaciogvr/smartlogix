package com.smartlogix.envio.repository;

import com.smartlogix.envio.model.IntentoEntrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IntentoEntregaRepository extends JpaRepository<IntentoEntrega, Long> {
    List<IntentoEntrega> findByEnvioId(Long envioId);
}
