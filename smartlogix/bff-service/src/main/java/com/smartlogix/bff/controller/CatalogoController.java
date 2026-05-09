package com.smartlogix.bff.controller;

import com.smartlogix.bff.client.InventoryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/catalogo")
public class CatalogoController {

    private final InventoryClient inventoryClient;

    public CatalogoController(InventoryClient inventoryClient) {
        this.inventoryClient = inventoryClient;
    }

    @GetMapping("/productos")
    public ResponseEntity<Object> productos() {
        return ResponseEntity.ok(inventoryClient.listarProductos());
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<Object> producto(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryClient.obtenerProducto(id));
    }

    @GetMapping("/productos/activos")
    public ResponseEntity<Object> activos() {
        return ResponseEntity.ok(inventoryClient.productosActivos());
    }

    @GetMapping("/productos/categoria/{categoria}")
    public ResponseEntity<Object> categoria(@PathVariable String categoria) {
        return ResponseEntity.ok(inventoryClient.porCategoria(categoria));
    }

    // 🔥 NUEVO
    @GetMapping("/productos/destacados")
    public ResponseEntity<Object> destacados() {
        return ResponseEntity.ok(inventoryClient.destacados());
    }

    // 🔥 NUEVO
    @GetMapping("/productos/stock/{id}")
    public ResponseEntity<Object> stock(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryClient.stock(id));
    }
}