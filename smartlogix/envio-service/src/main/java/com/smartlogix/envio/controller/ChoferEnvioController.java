package com.smartlogix.envio.controller;

import com.smartlogix.envio.dto.response.EnvioResponse;
import com.smartlogix.envio.service.EnvioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chofer/envios")
public class ChoferEnvioController {

    private final EnvioService envioService;

    public ChoferEnvioController(EnvioService envioService) {
        this.envioService = envioService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CHOFER', 'ADMIN')")
    public ResponseEntity<List<EnvioResponse>> listar(Authentication auth) {
        Jwt jwt = (Jwt) auth.getPrincipal();
        String choferId = jwt.getSubject();
        return ResponseEntity.ok(envioService.listarPorChofer(choferId));
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('CHOFER', 'ADMIN')")
    public ResponseEntity<EnvioResponse> actualizarEstado(
            @PathVariable Long id,
            @RequestParam String estado,
            Authentication auth) {
        return ResponseEntity.ok(envioService.actualizarEstado(id, estado));
    }

    @PostMapping("/{id}/entregar")
    @PreAuthorize("hasAnyRole('CHOFER', 'ADMIN')")
    public ResponseEntity<EnvioResponse> marcarEntregado(
            @PathVariable Long id,
            @RequestBody com.smartlogix.envio.dto.request.EntregarRequest request,
            Authentication auth) {
        Jwt jwt = (Jwt) auth.getPrincipal();
        String choferId = jwt.getSubject();
        return ResponseEntity.ok(envioService.marcarEntregado(id, request, choferId));
    }
}
