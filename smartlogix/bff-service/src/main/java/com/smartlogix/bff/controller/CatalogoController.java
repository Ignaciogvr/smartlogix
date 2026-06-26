package com.smartlogix.bff.controller;

import com.smartlogix.bff.dto.response.ProductoCatalogoDTO;
import com.smartlogix.bff.service.CatalogoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalogo")
public class CatalogoController {

    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping("/productos")
    public ResponseEntity<List<ProductoCatalogoDTO>> listar() {
        return ResponseEntity.ok(catalogoService.listarProductos());
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductoCatalogoDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.obtenerProducto(id));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ProductoCatalogoDTO>> activos() {
        return ResponseEntity.ok(catalogoService.listarProductosActivos());
    }

    @GetMapping("/destacados")
    public ResponseEntity<List<ProductoCatalogoDTO>> destacados() {
        return ResponseEntity.ok(catalogoService.productosDestacados());
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductoCatalogoDTO>> categoria(
            @PathVariable String categoria
    ) {
        return ResponseEntity.ok(
                catalogoService.listarPorCategoria(categoria)
        );
    }

    @GetMapping("/productos/{id}/comentarios")
    public ResponseEntity<List<com.smartlogix.bff.dto.response.ComentarioDTO>> comentarios(@PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.obtenerComentarios(id));
    }

    @GetMapping("/productos/{id}/relacionados")
    public ResponseEntity<List<ProductoCatalogoDTO>> relacionados(@PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.productosRelacionados(id));
    }

    @PostMapping("/productos/{id}/comentarios")
    public ResponseEntity<com.smartlogix.bff.dto.response.ComentarioDTO> agregarComentario(
            @PathVariable Long id,
            @RequestBody com.smartlogix.bff.dto.request.ComentarioCreateRequestDTO request) {
        String userId = com.smartlogix.bff.security.SecurityUtils.auth0Subject().orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
        // Extraemos nombre temporalmente genérico o de un claim si existiera. Asumimos "Usuario Auth0" si no tenemos un profile disponible.
        String userName = "Usuario Auth0"; 
        return ResponseEntity.ok(catalogoService.agregarComentario(id, request, userId, userName));
    }

    @PutMapping("/comentarios/{id}")
    public ResponseEntity<com.smartlogix.bff.dto.response.ComentarioDTO> actualizarComentario(
            @PathVariable Long id,
            @RequestBody com.smartlogix.bff.dto.request.ComentarioUpdateRequestDTO request) {
        String userId = com.smartlogix.bff.security.SecurityUtils.auth0Subject().orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
        return ResponseEntity.ok(catalogoService.actualizarComentario(id, request, userId));
    }

    @DeleteMapping("/comentarios/{id}")
    public ResponseEntity<Void> eliminarComentario(@PathVariable Long id) {
        String userId = com.smartlogix.bff.security.SecurityUtils.auth0Subject().orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
        catalogoService.eliminarComentario(id, userId);
        return ResponseEntity.ok().build();
    }
}