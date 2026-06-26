package com.smartlogix.bff.client;

import com.smartlogix.bff.client.dto.ServiceEnvelope;
import com.smartlogix.bff.dto.request.ActualizarPerfilRequest;
import com.smartlogix.bff.dto.response.UsuarioResponse;
import com.smartlogix.bff.security.SecurityUtils;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Component
public class UsuarioClient {

    private static final ParameterizedTypeReference<ServiceEnvelope<UsuarioResponse>> ENVELOPE_USUARIO =
            new ParameterizedTypeReference<>() {};

    private final WebClient webClient;

    public UsuarioClient(
            @Qualifier("usuariosWebClient")
            WebClient webClient
    ) {
        this.webClient = webClient;
    }

    public UsuarioResponse miPerfil() {
        ServiceEnvelope<UsuarioResponse> envelope = webClient
                .get()
                .uri("/usuarios/me")
                .retrieve()
                .bodyToMono(ENVELOPE_USUARIO)
                .block();

        return unwrapUsuario(envelope);
    }

    public Boolean existeUsuario(String usuarioId) {
        return webClient
                .get()
                .uri("/usuarios/internal/{id}", usuarioId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

    public UsuarioResponse obtenerUsuario(String usuarioId) {
        ServiceEnvelope<UsuarioResponse> envelope = webClient
                .get()
                .uri("/usuarios/{id}", usuarioId)
                .retrieve()
                .bodyToMono(ENVELOPE_USUARIO)
                .block();

        return unwrapUsuario(envelope);
    }

    public UsuarioResponse actualizarPerfil(ActualizarPerfilRequest request) {
        String subject = SecurityUtils.auth0Subject()
                .orElseThrow(() -> new IllegalStateException("No se pudo resolver el subject Auth0"));

        Map<String, String> body = new HashMap<>();
        if (request.getNombre() != null) {
            body.put("nombre", request.getNombre());
        }

        ServiceEnvelope<UsuarioResponse> envelope = webClient
                .put()
                .uri("/usuarios/{id}", subject)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(ENVELOPE_USUARIO)
                .block();

        return unwrapUsuario(envelope);
    }

    private static UsuarioResponse unwrapUsuario(ServiceEnvelope<UsuarioResponse> envelope) {
        return envelope != null ? envelope.getData() : null;
    }
}
