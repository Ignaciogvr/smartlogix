package com.smartlogix.bff.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${services.inventory.url:http://inventory-service:8080}")
    private String inventoryUrl;

    @Value("${services.pedidos.url:http://pedidos-service:8080}")
    private String pedidosUrl;

    @Value("${services.usuarios.url:http://usuarios-service:8080}")
    private String usuariosUrl;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient inventoryWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(inventoryUrl)
                .build();
    }

    @Bean
    public WebClient pedidosWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(pedidosUrl)
                .build();
    }

    @Bean
    public WebClient usuariosWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(usuariosUrl)
                .build();
    }
}