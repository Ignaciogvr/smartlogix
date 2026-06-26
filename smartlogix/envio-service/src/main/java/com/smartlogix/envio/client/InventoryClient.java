package com.smartlogix.envio.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class InventoryClient {

    private static final Logger log = LoggerFactory.getLogger(InventoryClient.class);

    private final WebClient webClient;

    public InventoryClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://inventory-service:8081").build();
    }

    public String verificarStock(String productoId, String token) {

        log.info("📦 Verificando stock productoId={}", productoId);

        return webClient.get()
                .uri("/api/inventory/" + productoId)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}