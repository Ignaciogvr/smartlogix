package com.smartlogix.bff.controller;

import com.smartlogix.bff.dto.request.ActualizarEstadoEnvioRequest;
import com.smartlogix.bff.dto.request.AsignarChoferRequest;
import com.smartlogix.bff.dto.request.CrearEnvioRequest;
import com.smartlogix.bff.dto.response.EnvioResponse;
import com.smartlogix.bff.dto.response.TrackingResponse;
import com.smartlogix.bff.service.EnvioBffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/envios")
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
    // MIS ENVIOS (para el usuario autenticado)
    // =========================

    @GetMapping("/mis-envios")
    public ResponseEntity<List<EnvioResponse>> misEnvios() {
        String userId = com.smartlogix.bff.security.SecurityUtils.auth0Subject()
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
        return ResponseEntity.ok(envioService.enviosUsuario(userId));
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
    // COTIZAR ENVIO (DEBE IR ANTES DE /{id})
    // =========================
    
    @GetMapping("/cotizar")
    public ResponseEntity<?> cotizar(
            @RequestParam String region,
            @RequestParam(required = false, defaultValue = "1") Double peso
    ) {
        return ResponseEntity.ok(
                envioService.cotizarEnvio(region, peso)
        );
    }
    
    // =========================
    // SEGUIMIENTO (alias de tracking por ID)
    // =========================
    
    @GetMapping("/{id}/seguimiento")
    public ResponseEntity<TrackingResponse> seguimiento(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                envioService.obtenerTrackingPorId(id)
        );
    }

    // =========================
    // OBTENER POR ID (DEBE IR AL FINAL)
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

    // =========================
    // ENVIOS POR PEDIDO
    // (Cliente → ver todos los envíos de su pedido sin código manual)
    // =========================

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<EnvioResponse>> porPedido(
            @PathVariable Long pedidoId
    ) {
        return ResponseEntity.ok(envioService.enviosPorPedido(pedidoId));
    }

    // =========================
    // ASIGNAR CHOFER
    // (ADMIN → asignar manualmente chofer a envío pendiente)
    // =========================

    /**
     * Endpoint para asignar un chofer a un envío pendiente.
     * Usado por ADMIN para asignar manualmente un chofer de la flota interna.
     * 
     * @param envioId ID del envío
     * @param request DTO con choferId y choferNombre
     * @return Envío actualizado con estado ASIGNADO
     */
    @PutMapping("/{envioId}/asignar-chofer")
    public ResponseEntity<EnvioResponse> asignarChofer(
            @PathVariable Long envioId,
            @RequestBody AsignarChoferRequest request
    ) {

        return ResponseEntity.ok(
                envioService.asignarChofer(envioId, request)
        );
    }
}

