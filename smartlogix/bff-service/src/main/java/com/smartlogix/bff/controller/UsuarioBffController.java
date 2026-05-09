package com.smartlogix.bff.controller;

import com.smartlogix.bff.client.UsuarioClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bff/usuarios")
public class UsuarioBffController {

    private final UsuarioClient usuarioClient;

    public UsuarioBffController(UsuarioClient usuarioClient) {
        this.usuarioClient = usuarioClient;
    }

    // 👤 PERFIL LOGUEADO (PRINCIPAL FRONTEND)
    @GetMapping("/me")
    public ResponseEntity<Object> me(@AuthenticationPrincipal Jwt jwt) {

        if (jwt == null) {
            return ResponseEntity.status(401).body("No autorizado");
        }

        String auth0Id = jwt.getSubject();

        return ResponseEntity.ok(
                usuarioClient.obtenerUsuario(auth0Id)
        );
    }

    // 👤 OBTENER USUARIO POR ID
    @GetMapping("/{auth0Id}")
    public ResponseEntity<Object> obtener(@PathVariable String auth0Id) {
        return ResponseEntity.ok(
                usuarioClient.obtenerUsuario(auth0Id)
        );
    }

    // 👥 LISTAR USUARIOS
    @GetMapping
    public ResponseEntity<Object> listar() {
        return ResponseEntity.ok(
                usuarioClient.listarUsuarios()
        );
    }

    // 🔍 EXISTS (para otros microservicios)
    @GetMapping("/exists/{auth0Id}")
    public ResponseEntity<Object> exists(@PathVariable String auth0Id) {
        return ResponseEntity.ok(
                usuarioClient.exists(auth0Id)
        );
    }

    // 🔄 REACTIVAR USUARIO (ADMIN)
    @PutMapping("/{auth0Id}/reactivar")
    public ResponseEntity<Object> reactivar(@PathVariable String auth0Id) {
        return ResponseEntity.ok(
                usuarioClient.reactivar(auth0Id)
        );
    }
}