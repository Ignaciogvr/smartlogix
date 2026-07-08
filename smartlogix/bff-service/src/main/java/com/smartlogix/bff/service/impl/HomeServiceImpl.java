package com.smartlogix.bff.service.impl;

import com.smartlogix.bff.client.InventoryClient;
import com.smartlogix.bff.client.dto.ServiceEnvelope;
import com.smartlogix.bff.dto.response.HomeResponse;
import com.smartlogix.bff.dto.response.ProductoCatalogoDTO;
import com.smartlogix.bff.service.HomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HomeServiceImpl implements HomeService {

    private static final Logger log = LoggerFactory.getLogger(HomeServiceImpl.class);

    private final InventoryClient inventoryClient;

    public HomeServiceImpl(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    private <T> List<T> safe(ServiceEnvelope<List<T>> env) {
        if (env == null || env.getData() == null) return List.of();
        return env.getData();
    }

    @Override
    public HomeResponse obtenerHome() {
        List<ProductoCatalogoDTO> destacados = safe(inventoryClient.productosDestacados());
        List<ProductoCatalogoDTO> ofertas = safe(inventoryClient.productosOfertas());
        List<ProductoCatalogoDTO> nuevos = safe(inventoryClient.productosNuevos());
        List<ProductoCatalogoDTO> todos = safe(inventoryClient.productosActivos());

        long categorias = todos.stream()
                .map(ProductoCatalogoDTO::getCategoria)
                .filter(c -> c != null && !c.isBlank())
                .distinct()
                .count();

        return new HomeResponse(destacados, ofertas, nuevos, todos.size(), (int) categorias);
    }

    @Override
    public List<ProductoCatalogoDTO> productosOfertas() {
        return safe(inventoryClient.productosOfertas());
    }

    @Override
    public List<ProductoCatalogoDTO> productosNuevos() {
        return safe(inventoryClient.productosNuevos());
    }

    @Override
    public List<Object> obtenerBanners() {
        try {
            ServiceEnvelope<List<Object>> env = inventoryClient.listarBanners();
            if (env == null || env.getData() == null) return List.of();
            return env.getData();
        } catch (Exception e) {
            log.warn("[HOME] No se pudieron cargar los banners: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public Map<String, Object> resumen() {
        List<ProductoCatalogoDTO> todos = safe(inventoryClient.productosActivos());
        long categorias = todos.stream()
                .map(ProductoCatalogoDTO::getCategoria)
                .filter(c -> c != null && !c.isBlank())
                .distinct()
                .count();
        return Map.of(
                "totalProductos", todos.size(),
                "totalCategorias", (int) categorias
        );
    }
}
