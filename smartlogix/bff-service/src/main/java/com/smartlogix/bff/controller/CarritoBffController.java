package com.smartlogix.bff.controller;

import com.smartlogix.bff.aggregator.CarritoAggregator;
import com.smartlogix.bff.client.PedidoClient;
import com.smartlogix.bff.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
public class CarritoBffController {

    private final PedidoClient pedidoClient;
    private final CarritoAggregator carritoAggregator;

    public CarritoBffController(PedidoClient pedidoClient, CarritoAggregator carritoAggregator) {
        this.pedidoClient = pedidoClient;
        this.carritoAggregator = carritoAggregator;
    }

    private String extractUserId(Jwt jwt) {
        return jwt.getSubject();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> obtenerCarrito(@AuthenticationPrincipal Jwt jwt) {
        String userId = extractUserId(jwt);
        var carritoEnriquecido = carritoAggregator.obtenerCarritoEnriquecido(userId);
        return ResponseEntity.ok(new ApiResponse<>("ok", "Carrito del usuario", carritoEnriquecido));
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<?>> agregarItem(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody java.util.Map<String, Object> request
    ) {
        String userId = extractUserId(jwt);
        
        // Obtener el precio del producto desde inventory-service
        Long productoId = ((Number) request.get("productoId")).longValue();
        Integer cantidad = ((Number) request.get("cantidad")).intValue();
        
        // Enriquecer con precio antes de enviar a pedidos-service
        var requestConPrecio = carritoAggregator.enriquecerItemConPrecio(productoId, cantidad);
        
        pedidoClient.agregarItemCarrito(userId, requestConPrecio);
        var carritoEnriquecido = carritoAggregator.obtenerCarritoEnriquecido(userId);
        return ResponseEntity.ok(new ApiResponse<>("ok", "Item agregado", carritoEnriquecido));
    }

    @PutMapping("/items/{productoId}")
    public ResponseEntity<ApiResponse<?>> actualizarItem(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long productoId,
            @RequestParam Integer cantidad
    ) {
        String userId = extractUserId(jwt);
        pedidoClient.actualizarItemCarrito(userId, productoId, cantidad);
        var carritoEnriquecido = carritoAggregator.obtenerCarritoEnriquecido(userId);
        return ResponseEntity.ok(new ApiResponse<>("ok", "Item actualizado", carritoEnriquecido));
    }

    @DeleteMapping("/items/{productoId}")
    public ResponseEntity<ApiResponse<?>> removerItem(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long productoId
    ) {
        String userId = extractUserId(jwt);
        pedidoClient.removerItemCarrito(userId, productoId);
        var carritoEnriquecido = carritoAggregator.obtenerCarritoEnriquecido(userId);
        return ResponseEntity.ok(new ApiResponse<>("ok", "Item eliminado", carritoEnriquecido));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<?>> vaciarCarrito(@AuthenticationPrincipal Jwt jwt) {
        String userId = extractUserId(jwt);
        pedidoClient.vaciarCarrito(userId);
        return ResponseEntity.ok(new ApiResponse<>("ok", "Carrito vaciado", null));
    }
}
