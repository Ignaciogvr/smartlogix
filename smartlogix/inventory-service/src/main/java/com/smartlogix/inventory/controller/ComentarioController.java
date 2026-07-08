package com.smartlogix.inventory.controller;

import com.smartlogix.inventory.dto.ComentarioUpdateRequest;
import com.smartlogix.inventory.dto.ResponderComentarioRequest;
import com.smartlogix.inventory.dto.ReportarComentarioRequest;
import com.smartlogix.inventory.exception.ApiResponse;
import com.smartlogix.inventory.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.smartlogix.inventory.dto.ComentarioModeracionRequestDTO;
import com.smartlogix.inventory.dto.ComentarioModeracionResponseDTO;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    private final ProductoService service;

    public ComentarioController(ProductoService service) {
        this.service = service;
    }

    /** Editar propio comentario */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> actualizarComentario(
            @PathVariable Long id,
            @RequestBody ComentarioUpdateRequest request,
            @RequestHeader("X-User-Id") String usuarioId) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Comentario actualizado", service.actualizarComentario(id, request, usuarioId))
        );
    }

    /** Borrar propio comentario (soft delete) */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> eliminarComentario(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") String usuarioId) {
        service.eliminarComentario(id, usuarioId);
        return ResponseEntity.ok(new ApiResponse(200, "Comentario eliminado", null));
    }

    /**
     * 2.7 - Responder comentario (solo ADMIN o VENDEDOR).
     * El rol se pasa via header X-User-Rol (inyectado por el BFF desde el JWT).
     */
    @PutMapping("/{id}/responder")
    public ResponseEntity<ApiResponse> responderComentario(
            @PathVariable Long id,
            @RequestBody ResponderComentarioRequest request,
            @RequestHeader("X-User-Rol") String rol) {
        return ResponseEntity.ok(new ApiResponse(200, "Respuesta agregada",
                service.responderComentario(id, request.getRespuesta(), rol)));
    }

    /**
     * 2.8 - Reportar comentario inapropiado (cualquier usuario autenticado).
     */
    @PostMapping("/{id}/reportar")
    public ResponseEntity<ApiResponse> reportarComentario(
            @PathVariable Long id,
            @RequestBody ReportarComentarioRequest request,
            @RequestHeader("X-User-Id") String usuarioId) {
        return ResponseEntity.ok(new ApiResponse(200, "Comentario reportado",
                service.reportarComentario(id, usuarioId, request.getMotivo())));
    }

    /**
     * 2.8 - Listar reportes pendientes (solo ADMIN).
     */
    @GetMapping("/reportes/pendientes")
    public ResponseEntity<ApiResponse> reportesPendientes() {
        return ResponseEntity.ok(new ApiResponse(200, "Reportes pendientes", service.reportesPendientes()));
    }

    /**
     * 2.15 - Marcar comentario como útil (cualquier usuario autenticado).
     */
    @PostMapping("/{id}/marcar-util")
    public ResponseEntity<ApiResponse> marcarUtil(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse(200, "Voto registrado", service.marcarUtil(id)));
    }

    /**
     * 2.15 - Destacar/des-destacar comentario (solo ADMIN).
     */
    @PutMapping("/{id}/destacar")
    public ResponseEntity<ApiResponse> marcarDestacado(
            @PathVariable Long id,
            @RequestParam(defaultValue = "true") Boolean destacado) {
        return ResponseEntity.ok(new ApiResponse(200, "Comentario actualizado",
                service.marcarDestacado(id, destacado)));
    }

    @PatchMapping("/{id}/moderar")
    public ResponseEntity<ApiResponse> moderarComentario(
            @PathVariable Long id,
            @RequestBody ComentarioModeracionRequestDTO request) {
        return ResponseEntity.ok(new ApiResponse(200, "Comentario moderado",
                service.moderarComentario(id, request)));
    }
}

