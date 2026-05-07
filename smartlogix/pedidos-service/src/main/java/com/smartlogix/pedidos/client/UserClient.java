package com.smartlogix.pedidos.client;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Component
public class UserClient {

    private final WebClient webClient;
    private final CircuitBreaker circuitBreaker;

    public UserClient(WebClient.Builder builder,
                      CircuitBreakerRegistry registry) {

        this.webClient = builder
                .baseUrl("http://usuarios-service:8080")
                .build();

        this.circuitBreaker = registry.circuitBreaker("user-service");
    }

    public CompletableFuture<Boolean> validarUsuario(String usuarioId) {

        Supplier<CompletableFuture<Boolean>> supplier = () ->
                webClient.get()
                        .uri("/usuarios/exists/{id}", usuarioId)
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .timeout(java.time.Duration.ofSeconds(3))
                        .toFuture();

        return circuitBreaker.executeSupplier(supplier);
    }
}