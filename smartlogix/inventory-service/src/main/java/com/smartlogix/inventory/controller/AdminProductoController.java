package com.smartlogix.inventory.controller;

import com.smartlogix.inventory.dto.ProductoCreateRequest;
import com.smartlogix.inventory.dto.ProductoUpdateRequest;
import com.smartlogix.inventory.exception.ApiResponse;
import com.smartlogix.inventory.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/productos")
public class AdminProductoController {

    private final ProductoService service;

    public AdminProductoController(ProductoService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse> crear(@RequestBody ProductoCreateRequest req) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Producto creado", service.crear(req))
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> actualizar(
            @PathVariable Long id,
            @RequestBody ProductoUpdateRequest req) {

        return ResponseEntity.ok(
                new ApiResponse(200, "Producto actualizado", service.actualizar(id, req))
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> eliminar(@PathVariable Long id) {
        service.eliminar(id);

        return ResponseEntity.ok(
                new ApiResponse(200, "Producto eliminado", null)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/reactivar")
    public ResponseEntity<ApiResponse> reactivar(@PathVariable Long id) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Producto reactivado", service.reactivar(id))
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bajo-stock")
    public ResponseEntity<ApiResponse> bajoStock() {
        return ResponseEntity.ok(
                new ApiResponse(200, "Productos con bajo stock", service.bajoStock())
        );
    }

    /**
     * Moderación de reviews por ADMIN.
     * Permite desactivar cualquier comentario sin importar el autor.
     * DELETE /admin/productos/comentarios/{comentarioId}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/comentarios/{comentarioId}")
    public ResponseEntity<ApiResponse> moderarComentario(@PathVariable Long comentarioId) {
        com.smartlogix.inventory.dto.ComentarioModeracionRequestDTO req = new com.smartlogix.inventory.dto.ComentarioModeracionRequestDTO();
        req.setAccion("ELIMINADO");
        req.setModeradorId(1L);
        service.moderarComentario(comentarioId, req);
        return ResponseEntity.ok(
                new ApiResponse(200, "Comentario moderado y desactivado", null)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats")
    public ResponseEntity<com.smartlogix.inventory.dto.StatsInventory> getStats(
            @org.springframework.beans.factory.annotation.Autowired com.smartlogix.inventory.repository.ProductoRepository productoRepository) {
        long total = productoRepository.count();
        long bajoStock = productoRepository.countByStockLessThan(10);
        long sinStock = productoRepository.countByStock(0);
        return ResponseEntity.ok(new com.smartlogix.inventory.dto.StatsInventory(total, bajoStock, sinStock));
    }
}