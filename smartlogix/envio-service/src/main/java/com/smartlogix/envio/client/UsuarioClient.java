package com.smartlogix.envio.client;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UsuarioClient {

    private final WebClient webClient;

    public UsuarioClient(WebClient.Builder builder) {

        this.webClient = builder
                .baseUrl("http://usuarios-service:8083")
                .build();
    }

    public Boolean obtenerUsuarioPorId(String id) {

        return webClient.get()
                .uri("/usuarios/internal/{id}", id)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        response -> response.bodyToMono(String.class)
                                .map(body ->
                                        new ResponseStatusException(
                                                response.statusCode(),
                                                body
                                        )
                                )
                )
                .bodyToMono(Boolean.class)
                .block();
    }
}