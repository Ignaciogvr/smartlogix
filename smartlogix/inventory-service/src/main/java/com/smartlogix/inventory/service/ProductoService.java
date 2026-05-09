package com.smartlogix.inventory.service;

import com.smartlogix.inventory.dto.ProductoCreateRequest;
import com.smartlogix.inventory.dto.ProductoResponse;
import com.smartlogix.inventory.dto.ProductoUpdateRequest;

import java.util.List;

public interface ProductoService {

    List<ProductoResponse> listar();
    List<ProductoResponse> activos();
    List<ProductoResponse> porCategoria(String categoria);
    List<ProductoResponse> bajoStock();

    ProductoResponse crear(ProductoCreateRequest request);
    ProductoResponse obtener(Long id);
    ProductoResponse actualizar(Long id, ProductoUpdateRequest request);

    void eliminar(Long id);
    ProductoResponse reactivar(Long id);

    ProductoResponse validarProducto(Long id, Integer cantidad);
    void descontarStock(Long id, Integer cantidad, String usuarioId);

    void agregarRating(Long id, Double rating);

    // 🔥 NUEVO (lo que te faltaba)
    List<ProductoResponse> destacados();

    Integer stock(Long id);
}