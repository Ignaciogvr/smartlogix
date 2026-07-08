package com.smartlogix.bff.service;

import com.smartlogix.bff.dto.response.HomeResponse;
import com.smartlogix.bff.dto.response.ProductoCatalogoDTO;

import java.util.List;
import java.util.Map;

public interface HomeService {
    HomeResponse obtenerHome();
    List<ProductoCatalogoDTO> productosOfertas();
    List<ProductoCatalogoDTO> productosNuevos();
    List<Object> obtenerBanners();
    Map<String, Object> resumen();
}
