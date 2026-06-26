package com.smartlogix.bff.controller;

import com.smartlogix.bff.dto.request.ActualizarEstadoEnvioRequest;
import com.smartlogix.bff.dto.request.CrearEnvioRequest;
import com.smartlogix.bff.dto.response.EnvioResponse;
import com.smartlogix.bff.dto.response.TrackingResponse;
import com.smartlogix.bff.service.EnvioBffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/envios")
public class EnvioBffController {

    private final EnvioBffService envioService;

    public EnvioBffController(
            EnvioBffService envioService
    ) {
        this.envioService = envioService;
    }

    // =========================
    // CREAR ENVIO
    // =========================

    @PostMapping
    public ResponseEntity<EnvioResponse> crear(
            @RequestBody CrearEnvioRequest request
    ) {

        return ResponseEntity.ok(
                envioService.crearEnvio(request)
        );
    }

    // =========================
    // LISTAR
    // =========================

    @GetMapping
    public ResponseEntity<List<EnvioResponse>> listar() {

        return ResponseEntity.ok(
                envioService.listarEnvios()
        );
    }

    // =========================
    // OBTENER
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<EnvioResponse> obtener(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                envioService.obtenerEnvio(id)
        );
    }

    // =========================
    // TRACKING
    // =========================

    @GetMapping("/tracking/{trackingCode}")
    public ResponseEntity<TrackingResponse> tracking(
            @PathVariable String trackingCode
    ) {

        return ResponseEntity.ok(
                envioService.obtenerTracking(trackingCode)
        );
    }

    // =========================
    // USUARIO
    // =========================

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EnvioResponse>> usuario(
            @PathVariable String usuarioId
    ) {

        return ResponseEntity.ok(
                envioService.enviosUsuario(usuarioId)
        );
    }

    // =========================
    // ACTUALIZAR ESTADO
    // =========================

    @PutMapping("/{envioId}/estado")
    public ResponseEntity<EnvioResponse> actualizarEstado(
            @PathVariable Long envioId,
            @RequestBody ActualizarEstadoEnvioRequest request
    ) {

        return ResponseEntity.ok(
                envioService.actualizarEstado(
                        envioId,
                        request
                )
        );
    }
}