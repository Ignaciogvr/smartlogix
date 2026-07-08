package com.smartlogix.inventory.repository;

import com.smartlogix.inventory.model.ComentarioModeracion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ComentarioModeracionRepository extends JpaRepository<ComentarioModeracion, Long> {
    Optional<ComentarioModeracion> findByComentarioId(Long comentarioId);
}
