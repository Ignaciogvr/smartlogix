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
import java.util.List;

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

    public UsuarioResponse crearDesdeToken() {
        ServiceEnvelope<UsuarioResponse> envelope = webClient
                .post()
                .uri("/usuarios/me")
                .retrieve()
                .bodyToMono(ENVELOPE_USUARIO)
                .block();

        return unwrapUsuario(envelope);
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

    private static final ParameterizedTypeReference<ServiceEnvelope<List<UsuarioResponse>>> ENVELOPE_USUARIOS =
            new ParameterizedTypeReference<>() {};

    public List<UsuarioResponse> listarUsuarios() {
        ServiceEnvelope<List<UsuarioResponse>> envelope = webClient
                .get()
                .uri("/usuarios")
                .retrieve()
                .bodyToMono(ENVELOPE_USUARIOS)
                .block();

        return envelope != null ? envelope.getData() : null;
    }

    public UsuarioResponse actualizarUsuario(String id, ActualizarPerfilRequest request) {
        Map<String, String> body = new HashMap<>();
        if (request.getNombre() != null) {
            body.put("nombre", request.getNombre());
        }

        ServiceEnvelope<UsuarioResponse> envelope = webClient
                .put()
                .uri("/usuarios/{id}", id)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(ENVELOPE_USUARIO)
                .block();

        return unwrapUsuario(envelope);
    }

    public void desactivarUsuario(String id) {
        webClient
                .delete()
                .uri("/usuarios/{id}", id)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void activarUsuario(String id) {
        webClient
                .put()
                .uri("/usuarios/{id}/reactivar", id)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void suspenderUsuario(String id, int dias) {
        webClient
                .put()
                .uri("/usuarios/{id}/suspender/{dias}", id, dias)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public UsuarioResponse crearVendedorOChofer(String nombre, String email, String rol, String documentoIdentidad) {
        Map<String, String> body = new HashMap<>();
        body.put("nombre", nombre);
        body.put("email", email);
        body.put("rol", rol);
        body.put("documentoIdentidad", documentoIdentidad);

        ServiceEnvelope<UsuarioResponse> envelope = webClient
                .post()
                .uri("/usuarios/crear")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(ENVELOPE_USUARIO)
                .block();

        return unwrapUsuario(envelope);
    }

    public UsuarioResponse cambiarRol(String id, String rol) {
        Map<String, String> body = new HashMap<>();
        body.put("rol", rol);

        ServiceEnvelope<UsuarioResponse> envelope = webClient
                .put()
                .uri("/usuarios/{id}/rol", id)
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
