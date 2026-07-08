package com.smartlogix.bff.mapper;

import com.smartlogix.bff.dto.response.UsuarioResponse;

import java.util.List;

public class UsuarioMapper {

    private UsuarioMapper() {
    }

    // =========================
    // CLONAR DTO
    // =========================

    public static UsuarioResponse toDTO(
            UsuarioResponse source
    ) {

        if (source == null) {
            return null;
        }

        UsuarioResponse dto =
                new UsuarioResponse();

        dto.setAuth0Id(
                source.getAuth0Id()
        );

        dto.setNombre(
                source.getNombre()
        );

        dto.setApellido(
                source.getApellido()
        );

        dto.setEmail(
                source.getEmail()
        );

        dto.setTelefono(
                source.getTelefono()
        );

        dto.setDireccion(
                source.getDireccion()
        );

        dto.setRol(
                source.getRol()
        );

        dto.setActivo(
                source.getActivo()
        );

        return dto;
    }

    // =========================
    // LISTAS
    // =========================

    public static List<UsuarioResponse> toDTOList(
            List<UsuarioResponse> usuarios
    ) {

        if (usuarios == null) {
            return List.of();
        }

        return usuarios.stream()
                .map(UsuarioMapper::toDTO)
                .toList();
    }
}