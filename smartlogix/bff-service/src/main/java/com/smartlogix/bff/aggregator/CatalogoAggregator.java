package com.smartlogix.bff.aggregator;

import com.smartlogix.bff.client.InventoryClient;
import com.smartlogix.bff.dto.response.ProductoCatalogoDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CatalogoAggregator {

    private final InventoryClient inventoryClient;

    public CatalogoAggregator(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    public List<ProductoCatalogoDTO> obtenerCatalogoCompleto() {
        var envelope = inventoryClient.listarProductos();
        return envelope != null && envelope.getData() != null
                ? envelope.getData()
                : List.of();
    }

    public List<ProductoCatalogoDTO> productosDestacados() {
        var envelope = inventoryClient.productosDestacados();
        return envelope != null && envelope.getData() != null
                ? envelope.getData()
                : List.of();
    }

    public List<ProductoCatalogoDTO> productosOferta() {
        var envelope = inventoryClient.productosDestacados();
        return envelope != null && envelope.getData() != null
                ? envelope.getData()
                : List.of();
    }

    public List<ProductoCatalogoDTO> productosNuevos() {
        var envelope = inventoryClient.productosDestacados();
        return envelope != null && envelope.getData() != null
                ? envelope.getData()
                : List.of();
    }

    public List<ProductoCatalogoDTO> porCategoria(String categoria) {
        var envelope = inventoryClient.productosPorCategoria(categoria);
        return envelope != null && envelope.getData() != null
                ? envelope.getData()
                : List.of();
    }
}