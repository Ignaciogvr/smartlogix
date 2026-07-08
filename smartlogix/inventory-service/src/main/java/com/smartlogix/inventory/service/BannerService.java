package com.smartlogix.inventory.service;

import com.smartlogix.inventory.dto.BannerRequest;
import com.smartlogix.inventory.dto.BannerResponse;
import java.util.List;

public interface BannerService {
    List<BannerResponse> listarActivos();
    List<BannerResponse> listarTodos();
    BannerResponse crear(BannerRequest request);
    BannerResponse actualizar(Long id, BannerRequest request);
    void eliminar(Long id);
}
