package com.smartlogix.envio.controller;

import com.smartlogix.envio.dto.request.ActualizarEstadoEnvioRequest;
import com.smartlogix.envio.dto.response.EnvioResponse;
import com.smartlogix.envio.service.EnvioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/envios")
public class AdminEnvioController {

    private final EnvioService envioService;

    public AdminEnvioController(EnvioService envioService) {
        this.envioService = envioService;
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<EnvioResponse> actualizarEstado(
            @PathVariable Long id,
            @RequestBody ActualizarEstadoEnvioRequest request
    ) {
        return ResponseEntity.ok(
                envioService.actualizarEstado(id, request.getEstado())
        );
    }
}