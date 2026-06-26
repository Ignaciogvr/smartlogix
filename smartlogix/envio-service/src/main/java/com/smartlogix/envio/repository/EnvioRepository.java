package com.smartlogix.envio.repository;

import com.smartlogix.envio.model.Envio;
import com.smartlogix.envio.model.EstadoEnvio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {

    Optional<Envio> findByTrackingNumber(String trackingNumber);

    List<Envio> findByPedidoId(Long pedidoId);

    List<Envio> findByUsuarioId(String usuarioId);

    List<Envio> findByEstado(EstadoEnvio estado);

    boolean existsByTrackingNumber(String trackingNumber);
}