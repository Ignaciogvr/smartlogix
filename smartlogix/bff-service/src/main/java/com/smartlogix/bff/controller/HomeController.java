package com.smartlogix.bff.controller;

import com.smartlogix.bff.dto.response.ApiResponse;
import com.smartlogix.bff.service.HomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> home() {
        return ResponseEntity.ok(new ApiResponse<>("ok", "Datos de inicio", homeService.obtenerHome()));
    }

    @GetMapping("/destacados")
    public ResponseEntity<ApiResponse<?>> destacados() {
        return ResponseEntity.ok(new ApiResponse<>("ok", "Productos destacados", homeService.obtenerHome().getDestacados()));
    }

    @GetMapping("/ofertas")
    public ResponseEntity<ApiResponse<?>> ofertas() {
        return ResponseEntity.ok(new ApiResponse<>("ok", "Productos en oferta", homeService.productosOfertas()));
    }

    @GetMapping("/nuevos")
    public ResponseEntity<ApiResponse<?>> nuevos() {
        return ResponseEntity.ok(new ApiResponse<>("ok", "Productos nuevos", homeService.productosNuevos()));
    }

    @GetMapping("/banners")
    public ResponseEntity<ApiResponse<?>> banners() {
        return ResponseEntity.ok(new ApiResponse<>("ok", "Banners activos", homeService.obtenerBanners()));
    }

    @GetMapping("/resumen")
    public ResponseEntity<ApiResponse<?>> resumen() {
        return ResponseEntity.ok(new ApiResponse<>("ok", "Resumen del catálogo", homeService.resumen()));
    }
}
