package com.smartlogix.bff.service.impl;

import com.smartlogix.bff.client.InventoryClient;
import com.smartlogix.bff.client.dto.ServiceEnvelope;
import com.smartlogix.bff.dto.response.ProductoCatalogoDTO;
import com.smartlogix.bff.service.BusquedaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusquedaServiceImpl implements BusquedaService {

    private final InventoryClient inventoryClient;

    public BusquedaServiceImpl(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    private <T> List<T> safe(ServiceEnvelope<List<T>> env) {
        if (env == null || env.getData() == null) return List.of();
        return env.getData();
    }

    @Override
    public List<ProductoCatalogoDTO> buscarProductos(String q) {
        return safe(inventoryClient.buscarProductos(q));
    }

    @Override
    public List<ProductoCatalogoDTO> filtrarProductos(Double precioMin, Double precioMax, String marca, Double ratingMin) {
        return safe(inventoryClient.filtrarProductos(precioMin, precioMax, marca, ratingMin));
    }
}
