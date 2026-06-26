package com.smartlogix.bff.controller;

import com.smartlogix.bff.dto.request.ActualizarPerfilRequest;
import com.smartlogix.bff.dto.response.UsuarioResponse;
import com.smartlogix.bff.service.UsuarioBffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
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
}