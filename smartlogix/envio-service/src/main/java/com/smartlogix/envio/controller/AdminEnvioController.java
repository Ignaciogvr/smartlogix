package com.smartlogix.envio.controller;

import com.smartlogix.envio.dto.request.ActualizarEstadoEnvioRequest;
import com.smartlogix.envio.dto.response.EnvioResponse;
import com.smartlogix.envio.model.EstadoEnvio;
import com.smartlogix.envio.model.Envio;
import com.smartlogix.envio.model.HistorialEnvio;
import com.smartlogix.envio.repository.EnvioRepository;
import com.smartlogix.envio.repository.HistorialEnvioRepository;
import com.smartlogix.envio.service.EnvioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/envios")
public class AdminEnvioController {

    private final EnvioService envioService;
    private final EnvioRepository envioRepository;
    private final HistorialEnvioRepository historialEnvioRepository;

    public AdminEnvioController(EnvioService envioService,
                                EnvioRepository envioRepository,
                                HistorialEnvioRepository historialEnvioRepository) {
        this.envioService = envioService;
        this.envioRepository = envioRepository;
        this.historialEnvioRepository = historialEnvioRepository;
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

    @PutMapping("/{id}/asignar-chofer")
    public ResponseEntity<EnvioResponse> asignarChofer(
            @PathVariable Long id,
            @RequestParam String choferId,
            @RequestParam String choferNombre) {
        return ResponseEntity.ok(envioService.asignarChofer(id, choferId, choferNombre));
    }

    /**
     * Tracking manual por Administrador.
     * Permite inyectar un evento de tracking arbitrario en el historial del envío.
     * Útil cuando el tracking automático falla o requiere corrección manual.
     * POST /api/admin/envios/{id}/tracking
     * Body: { "estado": "IN_TRANSIT", "descripcion": "Paquete retenido en aduana" }
     */
    @PostMapping("/{id}/tracking")
    public ResponseEntity<Map<String, Object>> trackingManual(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado: " + id));

        String descripcion = request.getOrDefault("descripcion", "Actualización manual de tracking");
        String estadoStr = request.get("estado");

        // Registrar en historial
        EstadoEnvio nuevoEstado = envio.getEstado();
        if (estadoStr != null) {
            try {
                nuevoEstado = EstadoEnvio.valueOf(estadoStr.toUpperCase());
                envio.setEstado(nuevoEstado);
                envioRepository.save(envio);
            } catch (IllegalArgumentException e) {
                // Estado inválido, solo agregar al historial sin cambiar estado
            }
        }

        HistorialEnvio historial = new HistorialEnvio(envio, nuevoEstado, "[MANUAL ADMIN] " + descripcion);
        historialEnvioRepository.save(historial);

        return ResponseEntity.ok(Map.of(
            "envioId", id,
            "estado", nuevoEstado.name(),
            "descripcion", descripcion,
            "mensaje", "Tracking manual registrado exitosamente"
        ));
    }
}