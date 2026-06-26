package com.smartlogix.inventory.controller;

import com.smartlogix.inventory.dto.ComentarioUpdateRequest;
import com.smartlogix.inventory.exception.ApiResponse;
import com.smartlogix.inventory.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    private final ProductoService service;

    public ComentarioController(ProductoService service) {
        this.service = service;
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> actualizarComentario(
            @PathVariable Long id,
            @RequestBody ComentarioUpdateRequest request,
            @RequestHeader("X-User-Id") String usuarioId) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Comentario actualizado", service.actualizarComentario(id, request, usuarioId))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> eliminarComentario(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") String usuarioId) {
        service.eliminarComentario(id, usuarioId);
        return ResponseEntity.ok(
                new ApiResponse(200, "Comentario eliminado", null)
        );
    }
}
