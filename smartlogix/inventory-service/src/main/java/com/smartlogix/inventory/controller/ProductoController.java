package com.smartlogix.inventory.controller;

import com.smartlogix.inventory.exception.ApiResponse;
import com.smartlogix.inventory.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // 🔓 LISTAR PRODUCTOS
    @GetMapping
    public ResponseEntity<ApiResponse> listar() {
        return ResponseEntity.ok(
                new ApiResponse(200, "Lista de productos", service.listar())
        );
    }

    // 🔓 ACTIVOS
    @GetMapping("/activos")
    public ResponseEntity<ApiResponse> activos() {
        return ResponseEntity.ok(
                new ApiResponse(200, "Productos activos", service.activos())
        );
    }

    // 🔓 DETALLE PRODUCTO
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Producto encontrado", service.obtener(id))
        );
    }

    // 🔓 POR CATEGORÍA
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<ApiResponse> porCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Productos por categoría", service.porCategoria(categoria))
        );
    }

    // 🔥 DESTACADOS (HOME FRONT)
    @GetMapping("/destacados")
    public ResponseEntity<ApiResponse> destacados() {
        return ResponseEntity.ok(
                new ApiResponse(200, "Productos destacados", service.destacados())
        );
    }

    // 🔥 STOCK (solo lectura)
    @GetMapping("/stock/{id}")
    public ResponseEntity<ApiResponse> stock(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Stock del producto", service.stock(id))
        );
    }
}