package com.smartlogix.pedidos.repository;

import com.smartlogix.pedidos.model.Devolucion;
import com.smartlogix.pedidos.model.EstadoDevolucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DevolucionRepository extends JpaRepository<Devolucion, Long> {
    List<Devolucion> findByPedidoId(Long pedidoId);
    List<Devolucion> findByUsuarioId(String usuarioId);
    List<Devolucion> findByEstadoDevolucion(EstadoDevolucion estadoDevolucion);
}
