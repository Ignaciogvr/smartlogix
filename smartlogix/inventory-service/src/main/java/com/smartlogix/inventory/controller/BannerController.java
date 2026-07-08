package com.smartlogix.inventory.controller;

import com.smartlogix.inventory.dto.BannerRequest;
import com.smartlogix.inventory.exception.ApiResponse;
import com.smartlogix.inventory.service.BannerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/banners")
public class BannerController {

    private final BannerService service;

    public BannerController(BannerService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> listarActivos() {
        return ResponseEntity.ok(
                new ApiResponse(200, "Banners activos", service.listarActivos())
        );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<ApiResponse> listarTodos() {
        return ResponseEntity.ok(
                new ApiResponse(200, "Todos los banners", service.listarTodos())
        );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse> crear(@Valid @RequestBody BannerRequest request) {
        return ResponseEntity.ok(
                new ApiResponse(201, "Banner creado", service.crear(request))
        );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> actualizar(@PathVariable Long id, @Valid @RequestBody BannerRequest request) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Banner actualizado", service.actualizar(id, request))
        );
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok(
                new ApiResponse(200, "Banner eliminado", null)
        );
    }
}
