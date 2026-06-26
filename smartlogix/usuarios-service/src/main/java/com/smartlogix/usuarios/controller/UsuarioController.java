package com.smartlogix.usuarios.controller;

import com.smartlogix.usuarios.exception.ApiResponse;
import com.smartlogix.usuarios.model.Usuario;
import com.smartlogix.usuarios.service.UsuarioService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/me")
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    public ResponseEntity<ApiResponse> crearDesdeToken(
            Authentication auth
    ) {
        log.info("POST /usuarios/me - Crear/obtener usuario desde token");

        Jwt jwt = (Jwt) auth.getPrincipal();

        String auth0Id = jwt.getSubject();

        String email = jwt.getClaimAsString("email");

        if (email == null) {
            email = "no-email";
        }

        String nombre = jwt.getClaimAsString("name");

        if (nombre == null) {
            nombre = jwt.getClaimAsString("nickname");
        }

        if (nombre == null) {
            nombre = "sin-nombre";
        }

        log.debug("Crear usuario desde token - auth0Id: {}, email: {}", auth0Id, email);

        Usuario usuario = service.crearDesdeToken(
                auth0Id,
                email,
                nombre
        );

        log.info("Usuario creado/obtenido exitosamente - auth0Id: {}", auth0Id);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ApiResponse(
                                201,
                                "Usuario creado/obtenido",
                                usuario
                        )
                );
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('CLIENTE','ADMIN')")
    public ResponseEntity<ApiResponse> miPerfil(
            Authentication auth
    ) {
        log.info("GET /usuarios/me - Obtener perfil del usuario");

        Jwt jwt = (Jwt) auth.getPrincipal();
        String auth0Id = jwt.getSubject();

        log.debug("Obteniendo perfil - auth0Id: {}", auth0Id);

        Usuario usuario = service.obtenerPorUserId(auth0Id);

        log.info("Perfil obtenido exitosamente - auth0Id: {}", auth0Id);

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Perfil del usuario",
                        usuario
                )
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> listar() {
        log.info("GET /usuarios - Listar todos los usuarios (ADMIN)");

        List<Usuario> usuarios = service.listar();

        log.info("Usuarios listados exitosamente - total: {}", usuarios.size());

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Lista usuarios",
                        usuarios
                )
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<ApiResponse> obtener(
            @PathVariable String id
    ) {
        log.info("GET /usuarios/{} - Obtener usuario por ID", id);

        Usuario usuario = service.obtenerPorUserId(id);

        log.info("Usuario obtenido exitosamente - auth0Id: {}", id);

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Usuario",
                        usuario
                )
        );
    }

    // ENDPOINT INTERNO MICROSERVICIOS
    @GetMapping("/internal/{id}")
    public ResponseEntity<Boolean> internalExists(
            @PathVariable String id
    ) {

        return ResponseEntity.ok(
                service.existePorAuth0Id(id)
        );
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<Boolean> exists(
            @PathVariable String id
    ) {

        return ResponseEntity.ok(
                service.existePorAuth0Id(id)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public ResponseEntity<ApiResponse> actualizar(
            @PathVariable String id,
            @RequestBody Map<String, String> body
    ) {
        log.info("PUT /usuarios/{} - Actualizar usuario", id);

        Usuario datos = new Usuario();

        if (body.get("nombre") != null) {
            datos.setNombre(body.get("nombre"));
            log.debug("Actualizando nombre para usuario: {}", id);
        }

        if (body.get("email") != null) {
            datos.setEmail(body.get("email"));
            log.debug("Actualizando email para usuario: {}", id);
        }

        Usuario usuarioActualizado = service.actualizarPorUserId(id, datos);

        log.info("Usuario actualizado exitosamente - auth0Id: {}", id);

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Actualizado",
                        usuarioActualizado
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> eliminar(
            @PathVariable String id
    ) {
        log.info("DELETE /usuarios/{} - Eliminar usuario (soft delete)", id);

        service.eliminarPorUserId(id);

        log.info("Usuario eliminado exitosamente (estado: INACTIVO) - auth0Id: {}", id);

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Eliminado",
                        null
                )
        );
    }

    @PutMapping("/{id}/reactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> reactivar(
            @PathVariable String id
    ) {

        Usuario usuario =
                service.obtenerPorUserId(id);

        usuario.setEstado("ACTIVO");

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Usuario reactivado",
                        service.actualizarPorUserId(
                                id,
                                usuario
                        )
                )
        );
    }
}