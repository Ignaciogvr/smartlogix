package com.smartlogix.inventory.mapper;

import com.smartlogix.inventory.dto.ProductoCreateRequest;
import com.smartlogix.inventory.dto.ProductoResponse;
import com.smartlogix.inventory.model.Producto;

public class ProductoMapper {

    public static Producto toEntity(ProductoCreateRequest req) {
        return new Producto(
                req.getNombre(),
                req.getDescripcion(),
                req.getPrecio(),
                req.getStock(),
                req.getCategoria(),
                req.getImagenes()
        );
    }

    public static ProductoResponse toResponse(Producto p) {
        return new ProductoResponse(
                p.getId(),
                p.getNombre(),
                p.getDescripcion(),
                p.getPrecio(),
                p.getStock(),
                p.getCategoria(),
                p.getImagenes(),
                p.getRatingPromedio(),
                p.getCantidadVendidos()
        );
    }
}