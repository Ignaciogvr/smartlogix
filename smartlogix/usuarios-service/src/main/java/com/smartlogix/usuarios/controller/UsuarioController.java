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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);
    private static final String NAMESPACED_ROLES_CLAIM = "https://smartlogix.com/roles";

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> crearDesdeToken(
            Authentication auth
    ) {
        log.info("POST /usuarios/me - Crear/obtener usuario desde token");

        Jwt jwt = (Jwt) auth.getPrincipal();

        Usuario usuario = sincronizarDesdeJwt(jwt);

        log.info("Usuario creado/obtenido exitosamente - auth0Id: {}", jwt.getSubject());

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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse> miPerfil(
            Authentication auth
    ) {
        log.info("GET /usuarios/me - Obtener perfil del usuario");

        Jwt jwt = (Jwt) auth.getPrincipal();
        Usuario usuario = sincronizarDesdeJwt(jwt);

        log.info("Perfil obtenido exitosamente - auth0Id: {}", jwt.getSubject());

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Perfil del usuario",
                        usuario
                )
        );
    }

    private Usuario sincronizarDesdeJwt(Jwt jwt) {
        String auth0Id = jwt.getSubject();

        String email = jwt.getClaimAsString("email");
        if (email == null || email.isBlank()) {
            email = auth0Id.replaceAll("[^A-Za-z0-9._-]", "_") + "@auth0.local";
        }

        String nombre = jwt.getClaimAsString("name");
        if (nombre == null || nombre.isBlank()) {
            nombre = jwt.getClaimAsString("nickname");
        }
        if (nombre == null || nombre.isBlank()) {
            nombre = auth0Id;
        }

        log.debug("Sincronizando usuario desde token - auth0Id: {}, email: {}", auth0Id, email);

        Usuario usuario = service.crearDesdeToken(auth0Id, email, nombre);
        com.smartlogix.usuarios.model.Rol rolToken = resolverRol(jwt);

        if (rolToken != null && usuario.getRol() != rolToken) {
            Usuario datos = new Usuario();
            datos.setRol(rolToken);
            usuario = service.actualizarPorUserId(auth0Id, datos);
        }

        return usuario;
    }

    private com.smartlogix.usuarios.model.Rol resolverRol(Jwt jwt) {
        List<String> claims = new ArrayList<>();

        List<String> namespacedRoles = jwt.getClaimAsStringList(NAMESPACED_ROLES_CLAIM);
        if (namespacedRoles != null) {
            claims.addAll(namespacedRoles);
        }

        List<String> permissions = jwt.getClaimAsStringList("permissions");
        if (permissions != null) {
            claims.addAll(permissions);
        }

        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles != null) {
            claims.addAll(roles);
        }

        String scope = jwt.getClaimAsString("scope");
        if (scope != null && !scope.isBlank()) {
            claims.addAll(List.of(scope.split(" ")));
        }

        return List.of("ADMIN", "VENDEDOR", "CHOFER", "CLIENTE").stream()
                .filter(prioridad -> claims.stream().anyMatch(claim -> prioridad.equalsIgnoreCase(claim)))
                .map(com.smartlogix.usuarios.model.Rol::valueOf)
                .findFirst()
                .orElse(null);
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

        if (body.get("telefono") != null) {
            datos.setTelefono(body.get("telefono"));
            log.debug("Actualizando telefono para usuario: {}", id);
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

    @PutMapping("/{id}/activar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> activar(
            @PathVariable String id
    ) {
        log.info("PUT /usuarios/{}/activar - Activar usuario (ADMIN)", id);

        Usuario usuarioActivado = service.activarUsuario(id);

        log.info("Usuario activado exitosamente - auth0Id: {}", id);

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Usuario activado",
                        usuarioActivado
                )
        );
    }

    @PutMapping("/{id}/desactivar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> desactivar(
            @PathVariable String id
    ) {
        log.info("PUT /usuarios/{}/desactivar - Desactivar usuario (ADMIN)", id);

        Usuario usuarioDesactivado = service.desactivarUsuario(id);

        log.info("Usuario desactivado exitosamente - auth0Id: {}", id);

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Usuario desactivado",
                        usuarioDesactivado
                )
        );
    }

    @PutMapping("/{id}/suspender/{dias}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> suspender(
            @PathVariable String id,
            @PathVariable int dias
    ) {
        log.info("PUT /usuarios/{}/suspender/{} - Suspender usuario por {} días (ADMIN)", id, dias, dias);

        if (dias <= 0) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(
                            400,
                            "Días debe ser mayor a 0",
                            null
                    ));
        }

        Usuario usuarioSuspendido = service.suspenderUsuario(id, dias);

        log.info("Usuario suspendido exitosamente - auth0Id: {}, dias: {}", id, dias);

        return ResponseEntity.ok(
                new ApiResponse(
                        200,
                        "Usuario suspendido por " + dias + " días",
                        usuarioSuspendido
                )
        );
    }

    @PostMapping("/crear")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> crearVendedorOChofer(
            @RequestBody Map<String, String> body
    ) {
        log.info("POST /usuarios/crear - Crear nuevo usuario (ADMIN)");

        String nombre = body.get("nombre");
        String email = body.get("email");
        String rol = body.get("rol");
        String documentoIdentidad = body.get("documentoIdentidad");

        if (nombre == null || nombre.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(400, "Nombre requerido", null));
        }

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(400, "Email requerido", null));
        }

        if (rol == null || rol.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(400, "Rol requerido", null));
        }

        if (!rol.equalsIgnoreCase("VENDEDOR") && !rol.equalsIgnoreCase("CHOFER")) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(400, "Rol debe ser VENDEDOR o CHOFER", null));
        }

        try {
            Usuario usuarioCreado = service.crearVendedorOChofer(nombre, email, rol, documentoIdentidad);

            log.info("Usuario {} creado exitosamente - email: {}", rol, email);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse(
                            201,
                            "Usuario " + rol.toLowerCase() + " creado correctamente",
                            usuarioCreado
                    ));
        } catch (Exception e) {
            log.error("Error creando usuario - error: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(400, e.getMessage(), null));
        }
    }

    @PutMapping("/{id}/rol")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> cambiarRol(
            @PathVariable String id,
            @RequestBody Map<String, String> body
    ) {
        log.info("PUT /usuarios/{}/rol - Cambiar rol de usuario (ADMIN)", id);

        String nuevoRol = body.get("rol");
        if (nuevoRol == null || nuevoRol.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(400, "El campo 'rol' es requerido", null));
        }

        try {
            Usuario usuario = service.cambiarRol(id, nuevoRol);
            return ResponseEntity.ok(new ApiResponse(200, "Rol actualizado a " + nuevoRol, usuario));
        } catch (Exception e) {
            log.error("Error cambiando rol - error: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(400, e.getMessage(), null));
        }
    }
}
