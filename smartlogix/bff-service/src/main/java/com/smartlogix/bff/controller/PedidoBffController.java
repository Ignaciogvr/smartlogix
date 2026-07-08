package com.smartlogix.bff.controller;

import com.smartlogix.bff.dto.request.CheckoutRequest;
import com.smartlogix.bff.dto.request.CompraRequest;
import com.smartlogix.bff.dto.response.EstadoCompletoResponse;
import com.smartlogix.bff.dto.response.PedidoResponse;
import com.smartlogix.bff.security.SecurityUtils;
import com.smartlogix.bff.service.PedidoBffService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoBffController {

    private final PedidoBffService pedidoService;

    public PedidoBffController(PedidoBffService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // =========================
    // CREAR
    // =========================

    @PostMapping
    public ResponseEntity<PedidoResponse> crear(
            @RequestBody CompraRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        if (request == null) {
            throw new IllegalArgumentException("Request no puede ser null");
        }
        // Asegurar que el usuarioId venga del JWT, no del body (evitar suplantación)
        if (request.getUsuarioId() == null || request.getUsuarioId().isBlank()) {
            request.setUsuarioId(jwt.getSubject());
        }
        return ResponseEntity.ok(pedidoService.crearPedido(request));
    }

    // =========================
    // CHECKOUT
    // =========================

    @PostMapping("/checkout")
    public ResponseEntity<PedidoResponse> checkout(
            @RequestBody CheckoutRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        if (request == null) {
            throw new IllegalArgumentException("Request no puede ser null");
        }
        // Inyectar userId desde JWT si no viene en el body
        if (request.getUsuarioId() == null || request.getUsuarioId().isBlank()) {
            request.setUsuarioId(jwt.getSubject());
        }
        return ResponseEntity.ok(pedidoService.checkout(request));
    }

    // =============================================
    // LISTAR — SEGURIDAD POR ROL
    // ADMIN → todos los pedidos (/admin/pedidos)
    // Cualquier otro rol → solo sus pedidos (/pedidos/usuario/{id})
    // =============================================

    @GetMapping
    public ResponseEntity<List<PedidoResponse>> listar(@AuthenticationPrincipal Jwt jwt) {
        if (SecurityUtils.hasRole("ADMIN")) {
            // ADMIN ve todos los pedidos
            return ResponseEntity.ok(pedidoService.listarPedidos());
        }
        // Usuario normal ve solo los suyos
        String userId = jwt.getSubject();
        return ResponseEntity.ok(pedidoService.pedidosUsuario(userId));
    }

    // =========================
    // OBTENER POR ID
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPedido(id));
    }

    // =========================
    // ESTADO COMPLETO
    // =========================

    @GetMapping("/{id}/estado-completo")
    public ResponseEntity<EstadoCompletoResponse> obtenerEstadoCompleto(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerEstadoCompleto(id));
    }

    // =========================
    // MIS PEDIDOS (usuario autenticado)
    // =========================

    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<PedidoResponse>> misPedidos(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(pedidoService.pedidosUsuario(jwt.getSubject()));
    }

    // =========================
    // PEDIDOS POR USUARIO (admin o vendedor)
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
    // CANCELAR
    // =========================

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
