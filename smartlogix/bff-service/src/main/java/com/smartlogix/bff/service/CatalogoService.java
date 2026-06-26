package com.smartlogix.bff.service;

import com.smartlogix.bff.dto.response.ProductoCatalogoDTO;

import java.util.List;

public interface CatalogoService {

    List<ProductoCatalogoDTO> listarProductos();

    ProductoCatalogoDTO obtenerProducto(
            Long productoId
    );

    List<ProductoCatalogoDTO> listarProductosActivos();

    List<ProductoCatalogoDTO> listarPorCategoria(
            String categoria
    );

    List<ProductoCatalogoDTO> productosDestacados();

    List<com.smartlogix.bff.dto.response.ComentarioDTO> obtenerComentarios(Long productoId);

    com.smartlogix.bff.dto.response.ComentarioDTO agregarComentario(Long productoId, com.smartlogix.bff.dto.request.ComentarioCreateRequestDTO request, String userId, String userName);

    com.smartlogix.bff.dto.response.ComentarioDTO actualizarComentario(Long comentarioId, com.smartlogix.bff.dto.request.ComentarioUpdateRequestDTO request, String userId);

    void eliminarComentario(Long comentarioId, String userId);

    List<ProductoCatalogoDTO> productosRelacionados(Long productoId);
}