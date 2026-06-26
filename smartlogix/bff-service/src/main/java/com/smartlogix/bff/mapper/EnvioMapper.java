package com.smartlogix.bff.mapper;

import com.smartlogix.bff.dto.response.EnvioResponse;
import com.smartlogix.bff.dto.response.TimelineResponse;
import com.smartlogix.bff.model.TrackingView;

import java.time.LocalDateTime;
import java.util.List;

public class EnvioMapper {

    private EnvioMapper() {
    }

    // =========================
    // MODEL -> DTO
    // =========================

    public static EnvioResponse toDTO(
            TrackingView model
    ) {

        if (model == null) {
            return null;
        }

        EnvioResponse dto =
                new EnvioResponse();

        dto.setPedidoId(
                model.getPedidoId()
        );

        dto.setTrackingCode(
                model.getTrackingCode()
        );

        dto.setEstado(
                model.getEstadoActual()
        );

        dto.setDireccionDestino(
                model.getUbicacionActual()
        );

        dto.setFechaEnvio(
                model.getFechaSalida()
        );

        dto.setFechaEntregaEstimada(
                model.getFechaEstimadaEntrega()
        );

        return dto;
    }

    // =========================
    // DTO -> MODEL
    // =========================

    public static TrackingView toModel(
            EnvioResponse dto
    ) {

        if (dto == null) {
            return null;
        }

        TrackingView model =
                new TrackingView();

        model.setPedidoId(
                dto.getPedidoId()
        );

        model.setTrackingCode(
                dto.getTrackingCode()
        );

        model.setEstadoActual(
                dto.getEstado()
        );

        model.setUbicacionActual(
                dto.getDireccionDestino()
        );

        model.setFechaSalida(
                dto.getFechaEnvio()
        );

        model.setFechaEstimadaEntrega(
                dto.getFechaEntregaEstimada()
        );

        return model;
    }

    // =========================
    // TIMELINE
    // =========================

    public static TimelineResponse toTimeline(
            String estado,
            String descripcion,
            LocalDateTime fecha
    ) {

        TimelineResponse response =
                new TimelineResponse();

        response.setEstado(estado);
        response.setDescripcion(descripcion);
        response.setFecha(fecha);

        return response;
    }

    // =========================
    // LISTAS
    // =========================

    public static List<EnvioResponse> toDTOList(
            List<TrackingView> models
    ) {

        if (models == null) {
            return List.of();
        }

        return models.stream()
                .map(EnvioMapper::toDTO)
                .toList();
    }

    public static List<TrackingView> toModelList(
            List<EnvioResponse> dtos
    ) {

        if (dtos == null) {
            return List.of();
        }

        return dtos.stream()
                .map(EnvioMapper::toModel)
                .toList();
    }
}