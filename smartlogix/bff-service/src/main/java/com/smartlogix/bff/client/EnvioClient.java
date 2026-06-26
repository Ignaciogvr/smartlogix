package com.smartlogix.bff.client;

import com.smartlogix.bff.client.dto.ErrorEnvelope;
import com.smartlogix.bff.dto.request.ActualizarEstadoEnvioRequest;
import com.smartlogix.bff.dto.request.CrearEnvioRequest;
import com.smartlogix.bff.dto.response.EnvioResponse;
import com.smartlogix.bff.exception.ExternalServiceException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class EnvioClient {

    private static final ParameterizedTypeReference<List<EnvioResponse>> LIST_ENVIO =
            new ParameterizedTypeReference<>() {};

    private final WebClient webClient;

    public EnvioClient(
            @Qualifier("enviosWebClient") WebClient webClient
    ) {
        this.webClient = webClient;
    }

    private Mono<? extends Throwable> handleError(ClientResponse response) {
        return response.bodyToMono(ErrorEnvelope.class)
                .map(error -> new ExternalServiceException("envio-service", error.getMessage() != null ? error.getMessage() : "Error desconocido"))
                .switchIfEmpty(Mono.error(new ExternalServiceException("envio-service", "Respuesta de error vacía del microservicio")));
    }

    public EnvioResponse crearEnvio(CrearEnvioRequest request) {
        return webClient.post()
                .uri("/api/envios")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(EnvioResponse.class)
                .block();
    }

    public List<EnvioResponse> listarEnvios() {
        return webClient.get()
                .uri("/api/envios")
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(LIST_ENVIO)
                .block();
    }

    public List<EnvioResponse> listarTodos() {
        return listarEnvios();
    }

    public EnvioResponse obtenerEnvio(Long envioId) {
        return webClient.get()
                .uri("/api/envios/{id}", envioId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(EnvioResponse.class)
                .block();
    }

    public EnvioResponse obtenerTracking(String trackingCode) {
        return webClient.get()
                .uri("/api/envios/tracking/{trackingCode}", trackingCode)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(EnvioResponse.class)
                .block();
    }

    public List<EnvioResponse> enviosUsuario(String usuarioId) {
        return webClient.get()
                .uri("/api/envios/usuario/{usuarioId}", usuarioId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(LIST_ENVIO)
                .block();
    }

    public List<EnvioResponse> enviosPorPedido(Long pedidoId) {
        return webClient.get()
                .uri("/api/envios/pedido/{pedidoId}", pedidoId)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(LIST_ENVIO)
                .block();
    }

    public EnvioResponse actualizarEstado(
            Long envioId,
            ActualizarEstadoEnvioRequest request
    ) {
        return webClient.put()
                .uri("/admin/envios/{id}/estado", envioId)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(EnvioResponse.class)
                .block();
    }
}
