package com.smartlogix.bff.mapper;

import com.smartlogix.bff.dto.response.CatalogoResponse;
import com.smartlogix.bff.model.CatalogoView;

public class CatalogoMapper {

    private CatalogoMapper() {
    }

    // =========================
    // MODEL -> DTO
    // =========================

    public static CatalogoResponse toDTO(
            CatalogoView view
    ) {

        if (view == null) {
            return null;
        }

        CatalogoResponse dto =
                new CatalogoResponse();

        dto.setCategoria(
                view.getCategoria()
        );

        dto.setTotalProductos(
                view.getTotalProductos()
        );

        dto.setProductosDisponibles(
                view.getProductosDisponibles()
        );

        dto.setProductosSinStock(
                view.getProductosSinStock()
        );

        dto.setProductosDestacados(
                view.getProductosDestacados()
        );

        dto.setProductosNuevos(
                view.getProductosNuevos()
        );

        dto.setProductosOferta(
                view.getProductosOferta()
        );

        dto.setPrecioMinimo(
                view.getPrecioMinimo()
        );

        dto.setPrecioMaximo(
                view.getPrecioMaximo()
        );

        dto.setMarcas(
                view.getMarcas()
        );

        return dto;
    }

    // =========================
    // DTO -> MODEL
    // =========================

    public static CatalogoView toModel(
            CatalogoResponse dto
    ) {

        if (dto == null) {
            return null;
        }

        CatalogoView view =
                new CatalogoView();

        view.setCategoria(
                dto.getCategoria()
        );

        view.setTotalProductos(
                dto.getTotalProductos()
        );

        view.setProductosDisponibles(
                dto.getProductosDisponibles()
        );

        view.setProductosSinStock(
                dto.getProductosSinStock()
        );

        view.setProductosDestacados(
                dto.getProductosDestacados()
        );

        view.setProductosNuevos(
                dto.getProductosNuevos()
        );

        view.setProductosOferta(
                dto.getProductosOferta()
        );

        view.setPrecioMinimo(
                dto.getPrecioMinimo()
        );

        view.setPrecioMaximo(
                dto.getPrecioMaximo()
        );

        view.setMarcas(
                dto.getMarcas()
        );

        return view;
    }
}