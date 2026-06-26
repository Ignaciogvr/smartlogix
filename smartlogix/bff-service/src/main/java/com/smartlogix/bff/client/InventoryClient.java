package com.smartlogix.bff.client;

import com.smartlogix.bff.client.dto.ServiceEnvelope;
import com.smartlogix.bff.dto.response.ProductoCatalogoDTO;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class InventoryClient {

    private static final ParameterizedTypeReference<ServiceEnvelope<List<ProductoCatalogoDTO>>> ENVELOPE_LIST =
            new ParameterizedTypeReference<>() {};

    private static final ParameterizedTypeReference<ServiceEnvelope<ProductoCatalogoDTO>> ENVELOPE_ONE =
            new ParameterizedTypeReference<>() {};

    private static final ParameterizedTypeReference<ServiceEnvelope<Integer>> ENVELOPE_INT =
            new ParameterizedTypeReference<>() {};

    private static final ParameterizedTypeReference<ServiceEnvelope<List<com.smartlogix.bff.dto.response.ComentarioDTO>>> ENVELOPE_COMMENTS =
            new ParameterizedTypeReference<>() {};

    private static final ParameterizedTypeReference<ServiceEnvelope<com.smartlogix.bff.dto.response.ComentarioDTO>> ENVELOPE_COMMENT =
            new ParameterizedTypeReference<>() {};

    private static final ParameterizedTypeReference<ServiceEnvelope<Void>> ENVELOPE_VOID =
            new ParameterizedTypeReference<>() {};

    private final WebClient webClient;

    public InventoryClient(
            @Qualifier("inventoryWebClient")
            WebClient webClient
    ) {
        this.webClient = webClient;
    }

    public ServiceEnvelope<List<ProductoCatalogoDTO>> listarProductos() {
        return webClient
                .get()
                .uri("/productos")
                .retrieve()
                .bodyToMono(ENVELOPE_LIST)
                .block();
    }

    public ServiceEnvelope<ProductoCatalogoDTO> obtenerProducto(Long id) {
        return webClient
                .get()
                .uri("/productos/{id}", id)
                .retrieve()
                .bodyToMono(ENVELOPE_ONE)
                .block();
    }

    public ServiceEnvelope<List<ProductoCatalogoDTO>> productosActivos() {
        return webClient
                .get()
                .uri("/productos/activos")
                .retrieve()
                .bodyToMono(ENVELOPE_LIST)
                .block();
    }

    public ServiceEnvelope<List<ProductoCatalogoDTO>> productosPorCategoria(String categoria) {
        return webClient
                .get()
                .uri("/productos/categoria/{categoria}", categoria)
                .retrieve()
                .bodyToMono(ENVELOPE_LIST)
                .block();
    }

    public ServiceEnvelope<List<ProductoCatalogoDTO>> productosDestacados() {
        return webClient
                .get()
                .uri("/productos/destacados")
                .retrieve()
                .bodyToMono(ENVELOPE_LIST)
                .block();
    }

    public ServiceEnvelope<Integer> stockProducto(Long id) {
        return webClient
                .get()
                .uri("/productos/stock/{id}", id)
                .retrieve()
                .bodyToMono(ENVELOPE_INT)
                .block();
    }

    public ServiceEnvelope<List<com.smartlogix.bff.dto.response.ComentarioDTO>> obtenerComentarios(Long id) {
        return webClient
                .get()
                .uri("/productos/{id}/comentarios", id)
                .retrieve()
                .bodyToMono(ENVELOPE_COMMENTS)
                .block();
    }

    public ServiceEnvelope<List<ProductoCatalogoDTO>> productosRelacionados(Long id) {
        return webClient
                .get()
                .uri("/productos/{id}/relacionados", id)
                .retrieve()
                .bodyToMono(ENVELOPE_LIST)
                .block();
    }

    public ServiceEnvelope<com.smartlogix.bff.dto.response.ComentarioDTO> agregarComentario(Long id, com.smartlogix.bff.dto.request.ComentarioCreateRequestDTO request, String userId, String userName) {
        return webClient
                .post()
                .uri("/productos/{id}/comentarios", id)
                .header("X-User-Id", userId)
                .header("X-User-Name", userName)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ENVELOPE_COMMENT)
                .block();
    }

    public ServiceEnvelope<com.smartlogix.bff.dto.response.ComentarioDTO> actualizarComentario(Long comentarioId, com.smartlogix.bff.dto.request.ComentarioUpdateRequestDTO request, String userId) {
        return webClient
                .put()
                .uri("/comentarios/{id}", comentarioId)
                .header("X-User-Id", userId)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ENVELOPE_COMMENT)
                .block();
    }

    public ServiceEnvelope<Void> eliminarComentario(Long comentarioId, String userId) {
        return webClient
                .delete()
                .uri("/comentarios/{id}", comentarioId)
                .header("X-User-Id", userId)
                .retrieve()
                .bodyToMono(ENVELOPE_VOID)
                .block();
    }
}
