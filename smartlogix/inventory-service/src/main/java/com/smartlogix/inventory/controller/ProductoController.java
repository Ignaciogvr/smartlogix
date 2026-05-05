package com.smartlogix.inventory.controller;

import com.smartlogix.inventory.model.Producto;
import com.smartlogix.inventory.service.ProductoService;
import com.smartlogix.inventory.exception.ApiResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> listar() {
        return ResponseEntity.ok(
                new ApiResponse(200, "Lista de productos", service.listar())
        );
    }

    @GetMapping("/activos")
    public ResponseEntity<ApiResponse> activos() {
        return ResponseEntity.ok(
                new ApiResponse(200, "Productos activos", service.activos())
        );
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<ApiResponse> porCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Productos por categoría", service.porCategoria(categoria))
        );
    }

    @GetMapping("/bajo-stock")
    public ResponseEntity<ApiResponse> bajoStock() {
        return ResponseEntity.ok(
                new ApiResponse(200, "Productos bajo stock", service.bajoStock())
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse> crear(@RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(201, "Producto creado", service.crear(producto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Producto encontrado", service.obtener(id))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Producto actualizado", service.actualizar(id, producto))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok(
                new ApiResponse(200, "Producto inactivado", null)
        );
    }

    @PutMapping("/{id}/reactivar")
    public ResponseEntity<ApiResponse> reactivar(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Producto reactivado", service.reactivar(id))
        );
    }

    @PutMapping("/{id}/rating/{valor}")
    public ResponseEntity<ApiResponse> rating(@PathVariable Long id, @PathVariable Double valor) {
        service.agregarRating(id, valor);
        return ResponseEntity.ok(
                new ApiResponse(200, "Rating agregado", null)
        );
    }

    @GetMapping("/{id}/validar/{cantidad}")
    public ResponseEntity<ApiResponse> validar(@PathVariable Long id, @PathVariable Integer cantidad) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Producto válido", service.validarProducto(id, cantidad))
        );
    }
}