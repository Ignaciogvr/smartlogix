package com.smartlogix.envio.controller;

import com.smartlogix.envio.dto.request.CrearEnvioRequest;
import com.smartlogix.envio.dto.response.EnvioResponse;
import com.smartlogix.envio.service.EnvioService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/envios")
public class EnvioController {

    private static final Logger log =
            LoggerFactory.getLogger(EnvioController.class);

    private final EnvioService envioService;

    public EnvioController(
            EnvioService envioService
    ) {
        this.envioService = envioService;
    }

    @PostMapping
    public ResponseEntity<EnvioResponse> crearEnvio(
            @RequestBody CrearEnvioRequest request
    ) {

        log.info("📥 POST /api/envios");

        return ResponseEntity.ok(
                envioService.crearEnvio(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<EnvioResponse>> listar() {

        log.info("📋 GET /api/envios (todos)");

        return ResponseEntity.ok(
                envioService.listarTodos()
        );
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EnvioResponse>> listarPorUsuario(
            @PathVariable String usuarioId
    ) {

        log.info("👤 GET /api/envios/usuario/{}", usuarioId);

        return ResponseEntity.ok(
                envioService.listarPorUsuario(usuarioId)
        );
    }

    @GetMapping("/tracking/{tracking}")
    public ResponseEntity<EnvioResponse> obtenerPorTracking(
            @PathVariable String tracking
    ) {

        log.info("🔎 GET tracking={}", tracking);

        return ResponseEntity.ok(
                envioService.obtenerPorTracking(tracking)
        );
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<EnvioResponse>> obtenerPorPedido(
            @PathVariable Long pedidoId
    ) {

        log.info("📦 GET envios pedido={}", pedidoId);

        return ResponseEntity.ok(
                envioService.obtenerPorPedido(pedidoId)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnvioResponse> obtenerPorId(
            @PathVariable Long id
    ) {

        log.info("🔎 GET envio id={}", id);

        return ResponseEntity.ok(
                envioService.obtenerPorId(id)
        );
    }
}