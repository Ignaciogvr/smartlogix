package com.smartlogix.envio.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ChilexpressClient {

    private final WebClient webClient;

    public ChilexpressClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://api.chilexpress.cl").build();
    }

    public String crearEnvio(Object request) {
        return webClient.post()
                .uri("/shipments")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String tracking(String trackingNumber) {
        return webClient.get()
                .uri("/tracking/" + trackingNumber)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}