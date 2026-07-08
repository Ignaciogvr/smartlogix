package com.smartlogix.bff.controller;

import com.smartlogix.bff.client.EnvioClient;
import com.smartlogix.bff.dto.response.EnvioResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chofer/envios")
@PreAuthorize("hasAnyRole('CHOFER', 'ADMIN')")
public class ChoferBffController {

    private final EnvioClient envioClient;

    public ChoferBffController(EnvioClient envioClient) {
        this.envioClient = envioClient;
    }

    @GetMapping
    public ResponseEntity<List<EnvioResponse>> getEnvios(Authentication auth) {
        Jwt jwt = (Jwt) auth.getPrincipal();
        String choferId = jwt.getSubject();
        List<EnvioResponse> envios = envioClient.enviosChofer(choferId);
        return ResponseEntity.ok(envios != null ? envios : List.of());
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<EnvioResponse> actualizarEstadoEnvio(
            @PathVariable Long id,
            @RequestBody Map<String, String> body,
            Authentication auth
    ) {
        String estado = body.get("estado");
        EnvioResponse result = envioClient.actualizarEstadoChofer(id, estado);
        return ResponseEntity.ok(result);
    }
}
