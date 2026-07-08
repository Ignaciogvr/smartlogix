package com.smartlogix.bff.controller;

import com.smartlogix.bff.dto.response.ProductoCatalogoDTO;
import com.smartlogix.bff.service.CatalogoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CatalogoController {

    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping("/productos")
    public ResponseEntity<List<ProductoCatalogoDTO>> listar() {
        return ResponseEntity.ok(catalogoService.listarProductos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ProductoCatalogoDTO>> activos() {
        return ResponseEntity.ok(catalogoService.listarProductosActivos());
    }

    @GetMapping("/productos/destacados")
    public ResponseEntity<List<ProductoCatalogoDTO>> destacados() {
        return ResponseEntity.ok(catalogoService.productosDestacados());
    }

    @GetMapping("/productos/ofertas")
    public ResponseEntity<List<ProductoCatalogoDTO>> ofertas() {
        return ResponseEntity.ok(catalogoService.productosEnOferta());
    }

    @GetMapping("/productos/nuevos")
    public ResponseEntity<List<ProductoCatalogoDTO>> nuevos() {
        return ResponseEntity.ok(catalogoService.productosNuevos());
    }

    @GetMapping("/productos/buscar")
    public ResponseEntity<List<ProductoCatalogoDTO>> buscar(@RequestParam String q) {
        return ResponseEntity.ok(catalogoService.buscarProductos(q));
    }

    @GetMapping("/productos/categoria/{categoria}")
    public ResponseEntity<List<ProductoCatalogoDTO>> categoria(
            @PathVariable String categoria
    ) {
        return ResponseEntity.ok(
                catalogoService.listarPorCategoria(categoria)
        );
    }

    @GetMapping("/productos/{id}")
    public ResponseEntity<ProductoCatalogoDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.obtenerProducto(id));
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
            @RequestBody com.smartlogix.bff.dto.request.ComentarioCreateRequestDTO request,
            @org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.oauth2.jwt.Jwt jwt) {
        String userId = com.smartlogix.bff.security.SecurityUtils.auth0Subject()
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
        // Extraer nombre real del JWT: name → nickname → "Usuario"
        String userName = com.smartlogix.bff.security.SecurityUtils.extractUserName(jwt);
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

    @GetMapping("/productos/{id}/relacionados-marca")
    public ResponseEntity<List<ProductoCatalogoDTO>> relacionadosPorMarca(@PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.productosRelacionadosPorMarca(id));
    }

    @GetMapping("/menos-vendidos")
    public ResponseEntity<List<ProductoCatalogoDTO>> menosVendidos() {
        return ResponseEntity.ok(catalogoService.productosMenosVendidos());
    }

    @PostMapping("/productos/{id}/vistas")
    public ResponseEntity<Void> registrarVista(@PathVariable Long id) {
        String userId = com.smartlogix.bff.security.SecurityUtils.auth0Subject().orElse(null);
        if (userId != null) {
            catalogoService.registrarVista(id, userId);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/vistos")
    public ResponseEntity<List<ProductoCatalogoDTO>> vistosRecientemente() {
        String userId = com.smartlogix.bff.security.SecurityUtils.auth0Subject().orElse(null);
        if (userId == null) return ResponseEntity.ok(java.util.Collections.emptyList());
        return ResponseEntity.ok(catalogoService.productosVistosRecientemente(userId));
    }

    @GetMapping("/recomendados")
    public ResponseEntity<List<ProductoCatalogoDTO>> recomendados() {
        String userId = com.smartlogix.bff.security.SecurityUtils.auth0Subject().orElse(null);
        // si userId es null, el service devolverá destacados por defecto
        return ResponseEntity.ok(catalogoService.productosRecomendados(userId));
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<String>> listarCategorias() {
        // Retornar categorías disponibles desde el enum o servicio
        return ResponseEntity.ok(catalogoService.listarCategorias());
    }

    @GetMapping("/banners")
    public ResponseEntity<List<java.util.Map<String, Object>>> listarBanners() {
        // Retornar banners promocionales
        return ResponseEntity.ok(catalogoService.listarBanners());
    }
}