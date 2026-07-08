package com.smartlogix.inventory.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import com.smartlogix.inventory.filter.RequestIdWebClientFilter;

@Configuration
public class WebClientConfig {

    // 🔥 fallback para evitar crash si no está en properties
    @Value("${services.base-url:http://usuarios-service:8083}")
    private String baseUrl;

    private final RequestIdWebClientFilter requestIdFilter;

    public WebClientConfig(RequestIdWebClientFilter requestIdFilter) {
        this.requestIdFilter = requestIdFilter;
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .filter(requestIdFilter)  // Propagar X-Request-Id
                .build();
    }
}