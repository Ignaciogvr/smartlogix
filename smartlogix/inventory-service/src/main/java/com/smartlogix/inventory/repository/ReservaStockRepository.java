package com.smartlogix.inventory.repository;

import com.smartlogix.inventory.model.ReservaStock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservaStockRepository extends JpaRepository<ReservaStock, Long> {
    List<ReservaStock> findByEstadoAndFechaExpiracionBefore(String estado, LocalDateTime fecha);
}
