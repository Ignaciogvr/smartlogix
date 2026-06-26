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
