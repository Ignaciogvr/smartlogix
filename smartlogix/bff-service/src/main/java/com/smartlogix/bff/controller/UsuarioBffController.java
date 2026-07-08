package com.smartlogix.bff.controller;

import com.smartlogix.bff.dto.request.ActualizarPerfilRequest;
import com.smartlogix.bff.dto.response.UsuarioResponse;
import com.smartlogix.bff.service.UsuarioBffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioBffController {

    private final UsuarioBffService usuarioService;

    public UsuarioBffController(
            UsuarioBffService usuarioService
    ) {
        this.usuarioService = usuarioService;
    }

    // =========================
    // PERFIL AUTH
    // =========================

    @PostMapping("/me")
    public ResponseEntity<UsuarioResponse> crearDesdeToken() {
        return ResponseEntity.status(201).body(
                usuarioService.crearDesdeToken()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> miPerfil() {

        return ResponseEntity.ok(
                usuarioService.miPerfil()
        );
    }

    // =========================
    // UPDATE PERFIL
    // =========================

    @PutMapping("/me")
    public ResponseEntity<UsuarioResponse> actualizar(
            @RequestBody ActualizarPerfilRequest request
    ) {

        return ResponseEntity.ok(
                usuarioService.actualizarPerfil(request)
        );
    }

    // =========================
    // VALIDAR USUARIO
    // =========================

    @GetMapping("/internal/{usuarioId}")
    public ResponseEntity<Boolean> existe(
            @PathVariable String usuarioId
    ) {
        return ResponseEntity.ok(
                usuarioService.existeUsuario(usuarioId)
        );
    }

    // =========================
    // ADMINISTRACIÓN DE USUARIOS
    // =========================

    @GetMapping
    public ResponseEntity<java.util.List<UsuarioResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> obtener(@PathVariable String id) {
        return ResponseEntity.ok(usuarioService.obtenerUsuario(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> actualizar(
            @PathVariable String id,
            @RequestBody ActualizarPerfilRequest request
    ) {
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, request));
    }

    @PutMapping("/{id}/activar")
    public ResponseEntity<Void> activar(@PathVariable String id) {
        usuarioService.activarUsuario(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable String id) {
        usuarioService.desactivarUsuario(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/suspender/{dias}")
    public ResponseEntity<Void> suspender(@PathVariable String id, @PathVariable int dias) {
        usuarioService.suspenderUsuario(id, dias);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/crear")
    public ResponseEntity<UsuarioResponse> crear(@RequestBody java.util.Map<String, String> body) {
        return ResponseEntity.ok(usuarioService.crearVendedorOChofer(
                body.get("nombre"),
                body.get("email"),
                body.get("rol"),
                body.get("documentoIdentidad")
        ));
    }

    @PutMapping("/{id}/rol")
    public ResponseEntity<UsuarioResponse> cambiarRol(
            @PathVariable String id,
            @RequestBody java.util.Map<String, String> body
    ) {
        return ResponseEntity.ok(usuarioService.cambiarRol(id, body.get("rol")));
    }
}