package com.smartlogix.envio.repository;

import com.smartlogix.envio.model.OptimizacionRuta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptimizacionRutaRepository extends JpaRepository<OptimizacionRuta, Long> {
}
