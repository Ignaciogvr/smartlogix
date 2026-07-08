package com.smartlogix.bff.controller;

import com.smartlogix.bff.client.PedidoClient;
import com.smartlogix.bff.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutBffController {

    private final PedidoClient pedidoClient;

    public CheckoutBffController(PedidoClient pedidoClient) {
        this.pedidoClient = pedidoClient;
    }

    private String extractUserId(Jwt jwt) {
        return jwt.getSubject();
    }

    /**
     * POST /api/checkout/confirmar
     * Procesa el checkout usando un Idempotency-Key para evitar pedidos duplicados.
     * Si el cliente no envía el header, se genera un UUID automáticamente.
     */
    @PostMapping("/confirmar")
    public ResponseEntity<ApiResponse<?>> confirmar(
            @AuthenticationPrincipal Jwt jwt,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @RequestBody Object request
    ) {
        if (request == null) {
            throw new IllegalArgumentException("Request no puede ser null");
        }

        String userId = extractUserId(jwt);

        // Si no viene Idempotency-Key, generar uno nuevo
        String finalKey = (idempotencyKey != null && !idempotencyKey.isBlank())
                ? idempotencyKey
                : UUID.randomUUID().toString();

        Object result = pedidoClient.confirmarCheckout(userId, finalKey, request);
        return ResponseEntity.ok(new ApiResponse<>("ok", "Pedido confirmado", result));
    }
    
    /**
     * POST /api/checkout
     * Alias del endpoint /confirmar para compatibilidad
     */
    @PostMapping
    public ResponseEntity<ApiResponse<?>> checkout(
            @AuthenticationPrincipal Jwt jwt,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @RequestBody Object request
    ) {
        return confirmar(jwt, idempotencyKey, request);
    }
}
