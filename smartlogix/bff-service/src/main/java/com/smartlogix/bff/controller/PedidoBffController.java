package com.smartlogix.bff.controller;

import com.smartlogix.bff.dto.request.CheckoutRequest;
import com.smartlogix.bff.dto.request.CompraRequest;
import com.smartlogix.bff.dto.response.PedidoResponse;
import com.smartlogix.bff.service.PedidoBffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoBffController {

    private final PedidoBffService pedidoService;

    public PedidoBffController(PedidoBffService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // =========================
    // CREAR
    // =========================

    @PostMapping
    public ResponseEntity<PedidoResponse> crear(@RequestBody CompraRequest request) {
        return ResponseEntity.ok(pedidoService.crearPedido(request));
    }

    // =========================
    // CHECKOUT
    // =========================

    @PostMapping("/checkout")
    public ResponseEntity<PedidoResponse> checkout(@RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(pedidoService.checkout(request));
    }

    // =========================
    // LISTAR
    // =========================

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listar() {
        return ResponseEntity.ok(pedidoService.listarPedidos());
    }

    // =========================
    // OBTENER
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPedido(id));
    }

    // =========================
    // ESTADO COMPLETO
    // =========================

    @GetMapping("/{id}/estado-completo")
    public ResponseEntity<com.smartlogix.bff.dto.response.EstadoCompletoResponse> obtenerEstadoCompleto(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(pedidoService.obtenerEstadoCompleto(id));
    }

    // =========================
    // USUARIO
    // =========================

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PedidoResponse>> usuario(@PathVariable String usuarioId) {
        return ResponseEntity.ok(pedidoService.pedidosUsuario(usuarioId));
    }

    // =========================
    // PAGAR
    // =========================

    @PutMapping("/{id}/pagar")
    public ResponseEntity<PedidoResponse> pagar(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.pagarPedido(id));
    }

    // =========================
    // CANCELAR (PUT hacia pedidos-service)
    // =========================

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
}