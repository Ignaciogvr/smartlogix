package com.smartlogix.bff.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UsuarioClient {

    private final WebClient webClient;

    public UsuarioClient(
            @Qualifier("usuariosWebClient")
            WebClient webClient
    ) {
        this.webClient = webClient;
    }

    // 🔥 LISTAR
    public Object listarUsuarios() {
        return webClient
                .get()
                .uri("/usuarios")
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }

    // 🔥 OBTENER
    public Object obtenerUsuario(String auth0Id) {
        return webClient
                .get()
                .uri("/usuarios/" + auth0Id)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }

    // 🔥 EXISTS
    public Object exists(String auth0Id) {
        return webClient
                .get()
                .uri("/usuarios/exists/" + auth0Id)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }
}