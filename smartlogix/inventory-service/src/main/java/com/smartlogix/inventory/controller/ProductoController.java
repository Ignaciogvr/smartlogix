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


    // 🔓 PUBLICO


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


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Producto encontrado", service.obtener(id))
        );
    }


    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<ApiResponse> porCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Productos por categoría", service.porCategoria(categoria))
        );
    }
}
