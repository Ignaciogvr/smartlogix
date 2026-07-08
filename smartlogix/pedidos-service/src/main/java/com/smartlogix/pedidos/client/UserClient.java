package com.smartlogix.pedidos.client;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Component
public class UserClient {

    private final WebClient webClient;
    private final CircuitBreaker circuitBreaker;

    public UserClient(WebClient.Builder builder,
                      CircuitBreakerRegistry registry) {

        this.webClient = builder
                .baseUrl("http://usuarios-service:8083")
                .build();

        this.circuitBreaker = registry.circuitBreaker("user-service");
    }

    public Boolean validarUsuario(String authHeader) {

        return circuitBreaker.executeSupplier(() ->
                webClient.get()
                        .uri("/usuarios/me")
                        .header("Authorization", authHeader)
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .timeout(Duration.ofSeconds(2))
                        .block()
        );
    }
}