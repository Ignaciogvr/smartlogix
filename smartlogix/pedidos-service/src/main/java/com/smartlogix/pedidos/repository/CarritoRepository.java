package com.smartlogix.pedidos.repository;

import com.smartlogix.pedidos.model.Carrito;
import com.smartlogix.pedidos.model.EstadoCarrito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsuarioIdAndEstado(String usuarioId, EstadoCarrito estado);
}
