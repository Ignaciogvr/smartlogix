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

    // 🔥 COMENTARIOS
    @GetMapping("/{id}/comentarios")
    public ResponseEntity<ApiResponse> obtenerComentarios(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Comentarios del producto", service.obtenerComentarios(id))
        );
    }

    @PostMapping("/{id}/comentarios")
    public ResponseEntity<ApiResponse> agregarComentario(
            @PathVariable Long id,
            @RequestBody com.smartlogix.inventory.dto.ComentarioCreateRequest request,
            @RequestHeader("X-User-Id") String usuarioId,
            @RequestHeader(value = "X-User-Name", defaultValue = "Usuario") String nombreCliente) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Comentario creado", service.agregarComentario(id, request, usuarioId, nombreCliente))
        );
    }

    // 🔥 RELACIONADOS
    @GetMapping("/{id}/relacionados")
    public ResponseEntity<ApiResponse> productosRelacionados(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Productos relacionados", service.productosRelacionados(id))
        );
    }

    // 🔥 STOCK (solo lectura)
    @GetMapping("/stock/{id}")
    public ResponseEntity<ApiResponse> stock(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Stock del producto", service.stock(id))
        );
    }

    @PutMapping("/stock/{id}/reponer")
    public ResponseEntity<ApiResponse> reponerStock(
            @PathVariable Long id,
            @RequestParam Integer cantidad
    ) {
        service.reponerStock(id, cantidad);
        return ResponseEntity.ok(
                new ApiResponse(200, "Stock repuesto correctamente", null)
        );
    }
}
