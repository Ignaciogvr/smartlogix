package com.smartlogix.pedidos.repository;

import com.smartlogix.pedidos.model.Pago;
import com.smartlogix.pedidos.model.EstadoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    
    /**
     * Buscar pagos por ID de pedido
     */
    List<Pago> findByPedidoId(Long pedidoId);
    
    /**
     * Buscar pagos por ID de usuario
     */
    List<Pago> findByUsuarioId(String usuarioId);
    
    /**
     * Buscar pagos por estado
     */
    List<Pago> findByEstadoPago(EstadoPago estadoPago);
    
    /**
     * Buscar pago por número de referencia único
     */
    Optional<Pago> findByNumeroReferencia(String numeroReferencia);
}
