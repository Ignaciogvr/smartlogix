package com.smartlogix.inventory.service.impl;

import com.smartlogix.inventory.dto.BannerRequest;
import com.smartlogix.inventory.dto.BannerResponse;
import com.smartlogix.inventory.model.Banner;
import com.smartlogix.inventory.repository.BannerRepository;
import com.smartlogix.inventory.service.BannerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;

    public BannerServiceImpl(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }

    private BannerResponse toResponse(Banner b) {
        return new BannerResponse(
                b.getId(),
                b.getTitulo(),
                b.getDescripcion(),
                b.getImagenUrl(),
                b.getRutaDestino(),
                b.getActivo(),
                b.getOrden(),
                b.getFechaCreacion(),
                b.getFechaActualizacion()
        );
    }

    @Override
    public List<BannerResponse> listarActivos() {
        return bannerRepository.findByActivoTrueOrderByOrdenAsc()
                .stream().map(this::toResponse).toList();
    }

    @Override
    public List<BannerResponse> listarTodos() {
        return bannerRepository.findAll()
                .stream().map(this::toResponse).toList();
    }

    @Override
    public BannerResponse crear(BannerRequest req) {
        Banner b = new Banner();
        b.setTitulo(req.getTitulo());
        b.setDescripcion(req.getDescripcion());
        b.setImagenUrl(req.getImagenUrl());
        b.setRutaDestino(req.getRutaDestino());
        b.setActivo(req.getActivo());
        b.setOrden(req.getOrden());
        return toResponse(bannerRepository.save(b));
    }

    @Override
    public BannerResponse actualizar(Long id, BannerRequest req) {
        Banner b = bannerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banner no encontrado"));
        b.setTitulo(req.getTitulo());
        b.setDescripcion(req.getDescripcion());
        b.setImagenUrl(req.getImagenUrl());
        b.setRutaDestino(req.getRutaDestino());
        b.setActivo(req.getActivo());
        b.setOrden(req.getOrden());
        return toResponse(bannerRepository.save(b));
    }

    @Override
    public void eliminar(Long id) {
        bannerRepository.deleteById(id);
    }
}
