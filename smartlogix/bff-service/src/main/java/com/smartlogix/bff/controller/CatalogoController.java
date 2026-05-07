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

    // 🔓 LISTAR PRODUCTOS
    @GetMapping("/productos")
    public ResponseEntity<Object> productos() {
        return ResponseEntity.ok(
                inventoryClient.listarProductos()
        );
    }

    // 🔓 PRODUCTO POR ID
    @GetMapping("/productos/{id}")
    public ResponseEntity<Object> producto(@PathVariable Long id) {
        return ResponseEntity.ok(
                inventoryClient.obtenerProducto(id)
        );
    }

    // 🔓 PRODUCTOS ACTIVOS
    @GetMapping("/productos/activos")
    public ResponseEntity<Object> activos() {
        return ResponseEntity.ok(
                inventoryClient.productosActivos()
        );
    }

    // 🔓 POR CATEGORIA
    @GetMapping("/productos/categoria/{categoria}")
    public ResponseEntity<Object> categoria(
            @PathVariable String categoria
    ) {

        return ResponseEntity.ok(
                inventoryClient.porCategoria(categoria)
        );
    }
}