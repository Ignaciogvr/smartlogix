package com.smartlogix.inventory.repository;

import com.smartlogix.inventory.model.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findByEstadoOrderByFechaCreacionAsc(String estado);
}
