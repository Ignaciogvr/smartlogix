package com.smartlogix.bff.service.impl;

import com.smartlogix.bff.client.InventoryClient;
import com.smartlogix.bff.client.dto.ServiceEnvelope;
import com.smartlogix.bff.dto.response.ProductoCatalogoDTO;
import com.smartlogix.bff.service.CatalogoService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CatalogoServiceImpl implements CatalogoService {

    private final InventoryClient inventoryClient;

    public CatalogoServiceImpl(
            InventoryClient inventoryClient
    ) {
        this.inventoryClient = inventoryClient;
    }

    @Override
    public List<ProductoCatalogoDTO> listarProductos() {
        return enrichList(dataOrEmpty(inventoryClient.listarProductos()));
    }

    @Override
    public ProductoCatalogoDTO obtenerProducto(Long productoId) {
        ServiceEnvelope<ProductoCatalogoDTO> envelope =
                inventoryClient.obtenerProducto(productoId);
        ProductoCatalogoDTO p = envelope != null ? envelope.getData() : null;
        enrich(p);
        return p;
    }

    @Override
    public List<ProductoCatalogoDTO> listarProductosActivos() {
        return enrichList(dataOrEmpty(inventoryClient.productosActivos()));
    }

    @Override
    public List<ProductoCatalogoDTO> listarPorCategoria(
            String categoria
    ) {
        return enrichList(dataOrEmpty(inventoryClient.productosPorCategoria(categoria)));
    }

    @Override
    public List<ProductoCatalogoDTO> productosDestacados() {
        return enrichList(dataOrEmpty(inventoryClient.productosDestacados()));
    }

    @Override
    public List<ProductoCatalogoDTO> productosEnOferta() {
        return enrichList(dataOrEmpty(inventoryClient.productosOfertas()));
    }

    @Override
    public List<ProductoCatalogoDTO> productosNuevos() {
        return enrichList(dataOrEmpty(inventoryClient.productosNuevos()));
    }

    @Override
    public List<com.smartlogix.bff.dto.response.ComentarioDTO> obtenerComentarios(Long productoId) {
        return dataOrEmpty(inventoryClient.obtenerComentarios(productoId));
    }

    @Override
    public List<ProductoCatalogoDTO> productosRelacionados(Long productoId) {
        return enrichList(dataOrEmpty(inventoryClient.productosRelacionados(productoId)));
    }

    @Override
    public com.smartlogix.bff.dto.response.ComentarioDTO agregarComentario(Long productoId, com.smartlogix.bff.dto.request.ComentarioCreateRequestDTO request, String userId, String userName) {
        ServiceEnvelope<com.smartlogix.bff.dto.response.ComentarioDTO> env = inventoryClient.agregarComentario(productoId, request, userId, userName);
        return env != null ? env.getData() : null;
    }

    @Override
    public com.smartlogix.bff.dto.response.ComentarioDTO actualizarComentario(Long comentarioId, com.smartlogix.bff.dto.request.ComentarioUpdateRequestDTO request, String userId) {
        ServiceEnvelope<com.smartlogix.bff.dto.response.ComentarioDTO> env = inventoryClient.actualizarComentario(comentarioId, request, userId);
        return env != null ? env.getData() : null;
    }

    @Override
    public void eliminarComentario(Long comentarioId, String userId) {
        inventoryClient.eliminarComentario(comentarioId, userId);
    }

    @Override
    public List<ProductoCatalogoDTO> productosRelacionadosPorMarca(Long productoId) {
        return enrichList(dataOrEmpty(inventoryClient.productosRelacionadosPorMarca(productoId)));
    }

    @Override
    public List<ProductoCatalogoDTO> productosMenosVendidos() {
        return enrichList(dataOrEmpty(inventoryClient.productosMenosVendidos()));
    }

    @Override
    public void registrarVista(Long productoId, String userId) {
        inventoryClient.registrarVista(productoId, userId);
    }

    @Override
    public List<ProductoCatalogoDTO> productosVistosRecientemente(String userId) {
        return enrichList(dataOrEmpty(inventoryClient.productosVistosRecientemente(userId)));
    }

    @Override
    public List<ProductoCatalogoDTO> productosRecomendados(String userId) {
        return enrichList(dataOrEmpty(inventoryClient.productosRecomendados(userId)));
    }

    @Override
    public List<String> listarCategorias() {
        // Retornar categorías disponibles
        return List.of("ELECTRONICA", "HOGAR", "DEPORTES", "MODA", "LIBROS", "JUGUETES", "ACCESORIOS", "OTROS");
    }

    @Override
    public List<java.util.Map<String, Object>> listarBanners() {
        // Retornar banners promocionales mock o desde servicio
        return List.of(
            java.util.Map.of(
                "id", 1,
                "titulo", "Ofertas de Temporada",
                "imagen", "/images/banner1.jpg",
                "link", "/catalogo/productos"
            ),
            java.util.Map.of(
                "id", 2,
                "titulo", "Nuevos Productos",
                "imagen", "/images/banner2.jpg",
                "link", "/catalogo/activos"
            )
        );
    }

    @Override
    public List<ProductoCatalogoDTO> buscarProductos(String query) {
        // Buscar productos por nombre o descripción
        return enrichList(dataOrEmpty(inventoryClient.buscarProductos(query)));
    }

    private static <T> List<T> dataOrEmpty(ServiceEnvelope<List<T>> envelope) {
        if (envelope == null || envelope.getData() == null) {
            return Collections.emptyList();
        }
        return envelope.getData();
    }

    private static void enrich(ProductoCatalogoDTO p) {
        if (p == null) {
            return;
        }
        // Debug: log para verificar imágenes recibidas del inventory
        if (p.getImagenes() != null && !p.getImagenes().isEmpty()) {
            System.out.println("[BFF] Producto: " + p.getNombre() + ", Imagen principal: " + p.getImagenPrincipal() + ", Imágenes: " + p.getImagenes());
        } else {
            System.out.println("[BFF] Producto: " + p.getNombre() + " - SIN IMÁGENES");
        }
        if (p.getActivo() == null && p.getEstadoProducto() != null) {
            p.setActivo("ACTIVO".equalsIgnoreCase(p.getEstadoProducto()));
        }
    }

    private static List<ProductoCatalogoDTO> enrichList(List<ProductoCatalogoDTO> list) {
        if (list == null) {
            return Collections.emptyList();
        }
        list.forEach(CatalogoServiceImpl::enrich);
        return list;
    }
}
