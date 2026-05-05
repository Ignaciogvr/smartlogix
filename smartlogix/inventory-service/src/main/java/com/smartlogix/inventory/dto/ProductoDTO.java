package com.smartlogix.inventory.dto;

public record ProductoDTO(
        Long id,
        String nombre,
        String descripcion,
        Double precio,
        Integer stock,
        String categoria,
        String imagenUrl,
        Double ratingPromedio,
        Integer cantidadVendidos,
        String estado
) {}