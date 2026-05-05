package com.smartlogix.inventory.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class UsuarioClient {

    private final WebClient webClient;

    public UsuarioClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://usuarios-service:8080")
                .build();
    }

    public boolean isUsuarioActivo(Long usuarioId, String token) {

        try {
            UsuarioDto usuario = webClient.get()
                    .uri("/usuarios/{id}", usuarioId)
                    .headers(headers -> headers.setBearerAuth(token))
                    .retrieve()
                    .bodyToMono(UsuarioDto.class)
                    .block();

            return usuario != null && "ACTIVO".equalsIgnoreCase(usuario.estado());

        } catch (WebClientResponseException.NotFound e) {
            return false;
        } catch (Exception e) {
            return false; // fallback seguro
        }
    }

    public record UsuarioDto(Long id, String nombre, String estado) {}
}