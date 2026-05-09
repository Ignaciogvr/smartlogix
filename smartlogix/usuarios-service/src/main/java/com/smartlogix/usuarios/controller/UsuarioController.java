package com.smartlogix.usuarios.controller;

import com.smartlogix.usuarios.model.Usuario;
import com.smartlogix.usuarios.service.UsuarioService;
import com.smartlogix.usuarios.exception.ApiResponse;

import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // 🔥 CREAR DESDE TOKEN
    @PostMapping("/me")
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    public ResponseEntity<ApiResponse> crearDesdeToken(Authentication auth) {

        Jwt jwt = (Jwt) auth.getPrincipal();

        String auth0Id = jwt.getSubject();

        String email = jwt.getClaimAsString("email");
        if (email == null) email = "no-email";

        String nombre = jwt.getClaimAsString("name");
        if (nombre == null) nombre = jwt.getClaimAsString("nickname");
        if (nombre == null) nombre = "sin-nombre";

        Usuario usuario = service.crearDesdeToken(auth0Id, email, nombre);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(201, "Usuario creado/obtenido", usuario));
    }

    // 👤 PERFIL (IMPORTANTE)
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    public ResponseEntity<ApiResponse> miPerfil(Authentication auth) {

        Jwt jwt = (Jwt) auth.getPrincipal();
        String auth0Id = jwt.getSubject();

        return ResponseEntity.ok(
                new ApiResponse(200, "Perfil del usuario",
                        service.obtenerPorUserId(auth0Id))
        );
    }

    // 🔥 LISTAR (ADMIN)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> listar() {
        return ResponseEntity.ok(
                new ApiResponse(200, "Lista usuarios", service.listar())
        );
    }

    // 🔥 OBTENER
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<ApiResponse> obtener(@PathVariable String id) {
        return ResponseEntity.ok(
                new ApiResponse(200, "Usuario", service.obtenerPorUserId(id))
        );
    }

    // 🔥 EXISTS (BFF interno)
    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> exists(@PathVariable String id) {
        return ResponseEntity.ok(service.existePorAuth0Id(id));
    }

    // 🔥 ACTUALIZAR
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<ApiResponse> actualizar(@PathVariable String id,
                @RequestBody Map<String, String> body) {

        Usuario datos = new Usuario();

        if (body.get("nombre") != null) {
            datos.setNombre(body.get("nombre"));
        }

        if (body.get("email") != null) {
            datos.setEmail(body.get("email"));
        }

        return ResponseEntity.ok(
                new ApiResponse(200, "Actualizado",
                        service.actualizarPorUserId(id, datos))
        );
    }

    // 🔥 ELIMINAR (soft delete)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> eliminar(@PathVariable String id) {
        service.eliminarPorUserId(id);
        return ResponseEntity.ok(new ApiResponse(200, "Eliminado", null));
    }

    // 🔄 REACTIVAR (ADMIN)
    @PutMapping("/{id}/reactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> reactivar(@PathVariable String id) {

        Usuario usuario = service.obtenerPorUserId(id);
        usuario.setEstado("ACTIVO");

        return ResponseEntity.ok(
                new ApiResponse(200, "Usuario reactivado",
                        service.actualizarPorUserId(id, usuario))
        );
    }
}