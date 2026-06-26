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
    void reponerStock(Long id, Integer cantidad);

    void agregarRating(Long id, Double rating);

    // 🔥 NUEVO (lo que te faltaba)
    List<ProductoResponse> destacados();

    List<com.smartlogix.inventory.dto.ComentarioResponse> obtenerComentarios(Long id);
    com.smartlogix.inventory.dto.ComentarioResponse agregarComentario(Long id, com.smartlogix.inventory.dto.ComentarioCreateRequest request, String usuarioId, String nombreCliente);
    com.smartlogix.inventory.dto.ComentarioResponse actualizarComentario(Long comentarioId, com.smartlogix.inventory.dto.ComentarioUpdateRequest request, String usuarioId);
    void eliminarComentario(Long comentarioId, String usuarioId);

    List<ProductoResponse> productosRelacionados(Long id);

    Integer stock(Long id);
}
