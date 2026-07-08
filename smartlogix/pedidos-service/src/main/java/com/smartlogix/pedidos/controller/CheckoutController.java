package com.smartlogix.pedidos.controller;

import com.smartlogix.pedidos.dto.CheckoutRequest;
import com.smartlogix.pedidos.service.CheckoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public ResponseEntity<?> procesarCheckout(
            @RequestHeader("X-User-Id") String usuarioId,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @RequestBody CheckoutRequest request) {
        return ResponseEntity.ok(checkoutService.procesarCheckout(usuarioId, idempotencyKey, request));
    }
}
