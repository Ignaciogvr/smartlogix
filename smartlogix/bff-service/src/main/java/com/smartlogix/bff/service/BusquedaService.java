package com.smartlogix.bff.service;

import com.smartlogix.bff.dto.response.ProductoCatalogoDTO;
import java.util.List;

public interface BusquedaService {
    List<ProductoCatalogoDTO> buscarProductos(String q);
    List<ProductoCatalogoDTO> filtrarProductos(Double precioMin, Double precioMax, String marca, Double ratingMin);
}
