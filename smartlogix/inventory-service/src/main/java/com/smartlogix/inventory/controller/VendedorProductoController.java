package com.smartlogix.inventory.controller;

import com.smartlogix.inventory.dto.ProductoCreateRequest;
import com.smartlogix.inventory.exception.ApiResponse;
import com.smartlogix.inventory.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vendedor/productos")
public class VendedorProductoController {

    private final ProductoService service;

    public VendedorProductoController(ProductoService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('VENDEDOR', 'ADMIN')")
    public ResponseEntity<ApiResponse> crear(@RequestBody ProductoCreateRequest req, Authentication auth) {
        Jwt jwt = (Jwt) auth.getPrincipal();
        String auth0Id = jwt.getSubject();
        req.setVendedorId(auth0Id);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(201, "Producto publicado exitosamente", service.crear(req)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('VENDEDOR', 'ADMIN')")
    public ResponseEntity<java.util.List<com.smartlogix.inventory.dto.ProductoResponse>> misProductos(Authentication auth) {
        Jwt jwt = (Jwt) auth.getPrincipal();
        String vendedorId = jwt.getSubject();
        return ResponseEntity.ok(service.listarPorVendedor(vendedorId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('VENDEDOR', 'ADMIN')")
    public ResponseEntity<com.smartlogix.inventory.dto.ProductoResponse> actualizar(
            @PathVariable Long id,
            @RequestBody com.smartlogix.inventory.dto.ProductoUpdateRequest request,
            Authentication auth
    ) {
        Jwt jwt = (Jwt) auth.getPrincipal();
        String vendedorId = jwt.getSubject();
        
        com.smartlogix.inventory.dto.ProductoResponse producto = service.obtener(id);
        
        if (!vendedorId.equals(producto.getVendedorId()) && !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new org.springframework.security.access.AccessDeniedException("No puedes editar este producto. No eres el vendedor propietario.");
        }
        
        return ResponseEntity.ok(service.actualizar(id, request));
    }
}


