package com.smartlogix.pedidos.controller;

import com.smartlogix.pedidos.dto.CarritoItemRequest;
import com.smartlogix.pedidos.service.CarritoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carritos")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    public ResponseEntity<?> obtenerCarrito(@RequestHeader("X-User-Id") String usuarioId) {
        return ResponseEntity.ok(carritoService.obtenerCarrito(usuarioId));
    }

    @PostMapping("/items")
    public ResponseEntity<?> agregarItem(
            @RequestHeader("X-User-Id") String usuarioId,
            @RequestBody CarritoItemRequest request) {
        return ResponseEntity.ok(carritoService.agregarItem(usuarioId, request));
    }

    @PutMapping("/items/{productoId}")
    public ResponseEntity<?> actualizarItem(
            @RequestHeader("X-User-Id") String usuarioId,
            @PathVariable Long productoId,
            @RequestParam Integer cantidad) {
        return ResponseEntity.ok(carritoService.actualizarItem(usuarioId, productoId, cantidad));
    }

    @DeleteMapping("/items/{productoId}")
    public ResponseEntity<?> removerItem(
            @RequestHeader("X-User-Id") String usuarioId,
            @PathVariable Long productoId) {
        return ResponseEntity.ok(carritoService.removerItem(usuarioId, productoId));
    }

    @DeleteMapping
    public ResponseEntity<?> vaciarCarrito(@RequestHeader("X-User-Id") String usuarioId) {
        carritoService.vaciarCarrito(usuarioId);
        return ResponseEntity.ok().build();
    }
}
