package com.smartlogix.bff.controller;

import com.smartlogix.bff.client.UsuarioClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bff/usuarios")
public class UsuarioBffController {

    private final UsuarioClient usuarioClient;

    public UsuarioBffController(UsuarioClient usuarioClient) {
        this.usuarioClient = usuarioClient;
    }

    // 🔥 PERFIL
    @GetMapping("/{auth0Id}")
    public ResponseEntity<Object> obtener(
            @PathVariable String auth0Id
    ) {

        return ResponseEntity.ok(
                usuarioClient.obtenerUsuario(auth0Id)
        );
    }

    // 🔥 LISTAR
    @GetMapping
    public ResponseEntity<Object> listar() {

        return ResponseEntity.ok(
                usuarioClient.listarUsuarios()
        );
    }

    // 🔥 EXISTE
    @GetMapping("/exists/{auth0Id}")
    public ResponseEntity<Object> exists(
            @PathVariable String auth0Id
    ) {

        return ResponseEntity.ok(
                usuarioClient.exists(auth0Id)
        );
    }
}