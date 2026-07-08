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

    // 🔥 NUEVO
    List<ProductoResponse> ofertas();
    List<ProductoResponse> nuevos();
    List<ProductoResponse> buscarPorTexto(String q);
    List<ProductoResponse> filtrar(Double precioMin, Double precioMax, String marca, Double ratingMin);

    ProductoResponse crear(ProductoCreateRequest request);
    ProductoResponse obtener(Long id);
    ProductoResponse actualizar(Long id, ProductoUpdateRequest request);

    void eliminar(Long id);
    ProductoResponse reactivar(Long id);

    ProductoResponse validarProducto(Long id, Integer cantidad);
    void descontarStock(Long id, Integer cantidad, String usuarioId);
    void reponerStock(Long id, Integer cantidad);

    void agregarRating(Long id, Double rating);

    List<ProductoResponse> destacados();

    List<com.smartlogix.inventory.dto.ComentarioResponse> obtenerComentarios(Long id);
    com.smartlogix.inventory.dto.ComentarioResponse agregarComentario(Long id, com.smartlogix.inventory.dto.ComentarioCreateRequest request, String usuarioId, String nombreCliente);
    com.smartlogix.inventory.dto.ComentarioResponse actualizarComentario(Long comentarioId, com.smartlogix.inventory.dto.ComentarioUpdateRequest request, String usuarioId);
    void eliminarComentario(Long comentarioId, String usuarioId);

    List<ProductoResponse> productosRelacionados(Long id);
    List<ProductoResponse> productosRelacionadosPorMarca(Long id);

    List<ProductoResponse> menosVendidos();

    void registrarVista(Long id, String usuarioId);
    List<ProductoResponse> vistosRecientemente(String usuarioId);
    List<ProductoResponse> recomendados(String usuarioId);


    Integer stock(Long id);

    /** Moderación de reviews por ADMIN: desactiva cualquier comentario */
    com.smartlogix.inventory.dto.ComentarioModeracionResponseDTO moderarComentario(Long comentarioId, com.smartlogix.inventory.dto.ComentarioModeracionRequestDTO request);

    List<ProductoResponse> listarPorVendedor(String vendedorId);

    // ===================== NUEVAS: SISTEMA DE COMENTARIOS AVANZADO =====================

    /** 2.7 - Responder comentario (ADMIN o VENDEDOR) */
    com.smartlogix.inventory.dto.ComentarioResponse responderComentario(Long comentarioId, String respuesta, String rol);

    /** 2.8 - Reportar comentario inapropiado */
    com.smartlogix.inventory.dto.ComentarioReporteResponse reportarComentario(Long comentarioId, String usuarioId, String motivo);

    /** 2.8 - Listar reportes pendientes (solo ADMIN) */
    List<com.smartlogix.inventory.dto.ComentarioReporteResponse> reportesPendientes();

    /** 2.14 - Distribución de ratings por estrella con porcentaje */
    List<com.smartlogix.inventory.dto.DistribucionRatingDTO> distribucionRatings(Long productoId);

    /** 2.15 - Marcar comentario como útil (suma 1 voto) */
    com.smartlogix.inventory.dto.ComentarioResponse marcarUtil(Long comentarioId);

    /** 2.15 - Destacar o des-destacar comentario (ADMIN) */
    com.smartlogix.inventory.dto.ComentarioResponse marcarDestacado(Long comentarioId, Boolean destacado);

    /** 2.15 - Obtener comentarios destacados de un producto */
    List<com.smartlogix.inventory.dto.ComentarioResponse> comentariosDestacados(Long productoId);
}

