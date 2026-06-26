package com.smartlogix.bff.controller;

import com.smartlogix.bff.dto.request.ActualizarEstadoEnvioRequest;
import com.smartlogix.bff.dto.response.EnvioResponse;
import com.smartlogix.bff.dto.response.PedidoResponse;
import com.smartlogix.bff.service.AdminBffService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador BFF para operaciones administrativas.
 * Expone endpoints /admin/** que requieren rol ADMIN.
 */
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBffController {

    private final AdminBffService adminService;

    public AdminBffController(AdminBffService adminService) {
        this.adminService = adminService;
    }

    // =========================
    // PEDIDOS - ADMIN
    // =========================

    @GetMapping("/pedidos")
    public ResponseEntity<List<PedidoResponse>> listarTodosPedidos() {
        return ResponseEntity.ok(adminService.listarTodosPedidos());
    }

    @GetMapping("/pedidos/estado/{estado}")
    public ResponseEntity<List<PedidoResponse>> pedidosPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(adminService.pedidosPorEstado(estado));
    }

    @PutMapping("/pedidos/{id}/preparar")
    public ResponseEntity<PedidoResponse> preparar(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.prepararPedido(id));
    }

    @PutMapping("/pedidos/{id}/enviar")
    public ResponseEntity<PedidoResponse> enviar(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.enviarPedido(id));
    }

    @PutMapping("/pedidos/{id}/entregar")
    public ResponseEntity<PedidoResponse> entregar(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.entregarPedido(id));
    }

    // =========================
    // ENVÍOS - ADMIN
    // =========================

    @GetMapping("/envios")
    public ResponseEntity<List<EnvioResponse>> listarTodosEnvios() {
        return ResponseEntity.ok(adminService.listarTodosEnvios());
    }

    @PutMapping("/envios/{id}/estado")
    public ResponseEntity<EnvioResponse> actualizarEstadoEnvio(
            @PathVariable Long id,
            @RequestBody ActualizarEstadoEnvioRequest request
    ) {
        return ResponseEntity.ok(adminService.actualizarEstadoEnvio(id, request));
    }
}
