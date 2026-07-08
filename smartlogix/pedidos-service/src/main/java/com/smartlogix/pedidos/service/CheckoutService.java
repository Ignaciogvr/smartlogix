package com.smartlogix.pedidos.service;

import com.smartlogix.pedidos.dto.CheckoutRequest;
import com.smartlogix.pedidos.dto.PedidoResponseDTO;

public interface CheckoutService {
    PedidoResponseDTO procesarCheckout(String usuarioId, String idempotencyKey, CheckoutRequest request);
}
