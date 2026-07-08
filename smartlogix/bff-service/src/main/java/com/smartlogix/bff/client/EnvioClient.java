package com.smartlogix.bff.client;

import com.smartlogix.bff.client.dto.ErrorEnvelope;
import com.smartlogix.bff.dto.request.ActualizarEstadoEnvioRequest;
import com.smartlogix.bff.dto.request.AsignarChoferRequest;
import com.smartlogix.bff.dto.request.CrearEnvioRequest;
import com.smartlogix.bff.dto.response.EnvioResponse;
import com.smartlogix.bff.dto.response.TrackingResponse;
import com.smartlogix.bff.exception.ExternalServiceException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class EnvioClient {

    private static final Logger log = LoggerFactory.getLogger(EnvioClient.class);

    private static final ParameterizedTypeReference<List<EnvioResponse>> LIST_ENVIO =
            new ParameterizedTypeReference<>() {};

    private final WebClient webClient;

    public EnvioClient(
            @Qualifier("enviosWebClient") WebClient webClient
    ) {
        this.webClient = webClient;
    }

    private Mono<? extends Throwable> handleError(ClientResponse response) {
        return response.bodyToMono(String.class)
                .map(body -> {
                    int code = response.statusCode().value();
                    // Propagar 4xx como WebClientResponseException para que GlobalExceptionHandler lo maneje correctamente
                    return (Throwable) new org.springframework.web.reactive.function.client.WebClientResponseException(
                            code,
                            response.statusCode().toString(),
                            response.headers().asHttpHeaders(),
                            body.getBytes(),
                            java.nio.charset.StandardCharsets.UTF_8
                    );
                })
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

    private static final ParameterizedTypeReference<List<EnvioResponse>> LIST_ENVIO_PLAIN =
            new ParameterizedTypeReference<>() {};

    public List<EnvioResponse> listarEnvios() {
        return webClient.get()
                .uri("/api/envios")
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(LIST_ENVIO_PLAIN)
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

    public TrackingResponse obtenerTracking(String trackingCode) {
        return webClient.get()
                .uri("/api/tracking/{trackingCode}", trackingCode)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(TrackingResponse.class)
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

    public EnvioResponse actualizarEstado(Long envioId, ActualizarEstadoEnvioRequest request) {
        return webClient.put()
                .uri("/api/envios/{id}/estado", envioId)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(EnvioResponse.class)
                .block();
    }

    // ===========================
    // CHOFER
    // ===========================

    public List<EnvioResponse> enviosChofer(String choferId) {
        log.info("[BFF] Obteniendo envíos para choferId: {}", choferId);
        return webClient.get()
                .uri("/api/chofer/envios")
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(LIST_ENVIO)
                .block();
    }

    public EnvioResponse actualizarEstadoChofer(Long id, String nuevoEstado) {
        log.info("[BFF] Chofer actualizando estado de envío {}: {}", id, nuevoEstado);
        return webClient.put()
                .uri(builder -> builder.path("/api/chofer/envios/{id}/estado")
                        .queryParam("estado", nuevoEstado)
                        .build(id))
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(EnvioResponse.class)
                .block();
    }

    // ===========================
    // ADMIN
    // ===========================

    /**
     * Asigna un chofer a un envío pendiente.
     * Usado por ADMIN para asignar manualmente un chofer de la flota interna.
     */
    public EnvioResponse asignarChofer(Long envioId, AsignarChoferRequest request) {
        log.info("[BFF-Client] Asignando chofer {} al envío {}", request.getChoferId(), envioId);
        
        return webClient.put()
                .uri("/api/envios/{envioId}/asignar-chofer", envioId)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(EnvioResponse.class)
                .block();
    }
    
    /**
     * Cotiza un envío según región y peso
     */
    public Object cotizarEnvio(String region, Double peso) {
        log.info("[BFF-Client] Cotizando envío: region={}, peso={}", region, peso);
        
        return webClient.get()
                .uri(builder -> builder.path("/api/envios/cotizar")
                        .queryParam("region", region)
                        .queryParam("peso", peso)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleError)
                .bodyToMono(Object.class)
                .block();
    }
}
