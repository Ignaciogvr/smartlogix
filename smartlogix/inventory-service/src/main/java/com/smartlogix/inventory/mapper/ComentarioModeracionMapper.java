package com.smartlogix.inventory.mapper;

import com.smartlogix.inventory.dto.ComentarioModeracionRequestDTO;
import com.smartlogix.inventory.dto.ComentarioModeracionResponseDTO;
import com.smartlogix.inventory.model.ComentarioModeracion;
import java.time.format.DateTimeFormatter;

public class ComentarioModeracionMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ComentarioModeracion toEntity(ComentarioModeracionRequestDTO dto) {
        ComentarioModeracion entity = new ComentarioModeracion();
        entity.setAccion(com.smartlogix.inventory.model.ModeracionAccion.valueOf(dto.getAccion()));
        entity.setModeradorId(dto.getModeradorId());
        entity.setFechaAccion(java.time.LocalDateTime.now());
        return entity;
    }

    public static ComentarioModeracionResponseDTO toDto(ComentarioModeracion entity) {
        return new ComentarioModeracionResponseDTO(
                entity.getId(),
                entity.getComentarioId(),
                entity.getModeradorId(),
                entity.getAccion().name(),
                entity.getFechaAccion().format(FORMATTER)
        );
    }
}
