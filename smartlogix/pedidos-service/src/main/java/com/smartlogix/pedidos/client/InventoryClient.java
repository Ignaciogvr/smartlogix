package com.smartlogix.pedidos.client;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Component
public class InventoryClient {

    private final WebClient webClient;
    private final CircuitBreaker circuitBreaker;

    public InventoryClient(WebClient.Builder builder,
                           CircuitBreakerRegistry registry) {

        this.webClient = builder
                .baseUrl("http://inventory-service:8081")
                .build();

        this.circuitBreaker = registry.circuitBreaker("inventory-service");
    }

    public CompletableFuture<Boolean> validarStock(Long productoId, Integer cantidad) {

        Supplier<CompletableFuture<Boolean>> supplier = () ->
                webClient.get()
                        .uri("/productos/{id}/validar/{cantidad}", productoId, cantidad)
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .timeout(java.time.Duration.ofSeconds(3))
                        .toFuture();

        return circuitBreaker.executeSupplier(supplier);
    }
}