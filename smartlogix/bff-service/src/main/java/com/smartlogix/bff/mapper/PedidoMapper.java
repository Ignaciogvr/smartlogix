package com.smartlogix.bff.mapper;

import com.smartlogix.bff.dto.response.PedidoResponse;
import com.smartlogix.bff.model.PedidoResumen;

import java.util.List;

public class PedidoMapper {

    private PedidoMapper() {
    }

    // =========================
    // MODEL -> DTO
    // =========================

    public static PedidoResponse toDTO(
            PedidoResumen model
    ) {

        if (model == null) {
            return null;
        }

        PedidoResponse dto =
                new PedidoResponse();

        dto.setId(
                model.getPedidoId()
        );

        dto.setUsuarioId(
                model.getUsuarioId()
        );

        dto.setEstado(
                model.getEstado()
        );

        dto.setTotal(
                model.getTotal()
        );

        dto.setFecha(
                model.getFechaPedido()
        );

        return dto;
    }

    // =========================
    // DTO -> MODEL
    // =========================

    public static PedidoResumen toModel(
            PedidoResponse dto
    ) {

        if (dto == null) {
            return null;
        }

        PedidoResumen model =
                new PedidoResumen();

        model.setPedidoId(
                dto.getId()
        );

        model.setUsuarioId(
                dto.getUsuarioId()
        );

        model.setEstado(
                dto.getEstado()
        );

        model.setTotal(
                dto.getTotal()
        );

        model.setFechaPedido(
                dto.getFecha()
        );

        return model;
    }

    // =========================
    // LISTAS
    // =========================

    public static List<PedidoResponse> toDTOList(
            List<PedidoResumen> models
    ) {

        if (models == null) {
            return List.of();
        }

        return models.stream()
                .map(PedidoMapper::toDTO)
                .toList();
    }

    public static List<PedidoResumen> toModelList(
            List<PedidoResponse> dtos
    ) {

        if (dtos == null) {
            return List.of();
        }

        return dtos.stream()
                .map(PedidoMapper::toModel)
                .toList();
    }
}