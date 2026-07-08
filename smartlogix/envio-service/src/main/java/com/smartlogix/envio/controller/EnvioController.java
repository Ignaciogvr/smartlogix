package com.smartlogix.envio.controller;

import com.smartlogix.envio.dto.AsignarChoferRequest;
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
    
    /**
     * Endpoint para cotizar un envío según región y peso.
     * Retorna el costo estimado del envío.
     * DEBE IR ANTES DE /{id} para evitar conflictos
     */
    @GetMapping("/cotizar")
    public ResponseEntity<?> cotizarEnvio(
            @RequestParam String region,
            @RequestParam(required = false, defaultValue = "1.0") Double peso
    ) {
        log.info("💰 GET /api/envios/cotizar - region={}, peso={}", region, peso);
        
        // Lógica simple de cotización
        double costoBase = 5000; // CLP
        double costoPorKg = 1000;
        double costoRegion = region.equalsIgnoreCase("RM") ? 0 : 2000;
        
        double total = costoBase + (peso * costoPorKg) + costoRegion;
        
        return ResponseEntity.ok(java.util.Map.of(
            "region", region,
            "peso", peso,
            "costoBase", costoBase,
            "costoPorKg", costoPorKg,
            "costoRegion", costoRegion,
            "total", total,
            "moneda", "CLP"
        ));
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

    /**
     * Endpoint para asignar un chofer a un envío.
     * SOLO para envíos en estado PENDIENTE.
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

        log.info("🚚 PUT /api/envios/{}/asignar-chofer - choferId={}", 
                envioId, request.getChoferId());

        return ResponseEntity.ok(
                envioService.asignarChofer(
                        envioId,
                        request.getChoferId(),
                        request.getChoferNombre()
                )
        );
    }
}