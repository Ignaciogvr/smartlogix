package com.smartlogix.bff.client;

import com.smartlogix.bff.client.dto.ServiceEnvelope;
import com.smartlogix.bff.dto.request.CompraRequest;
import com.smartlogix.bff.dto.response.PedidoResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class PedidoClient {

    private static final Logger log = LoggerFactory.getLogger(PedidoClient.class);

    private static final ParameterizedTypeReference<ServiceEnvelope<PedidoResponse>> ENVELOPE_PEDIDO =
            new ParameterizedTypeReference<>() {};

    private static final ParameterizedTypeReference<ServiceEnvelope<List<PedidoResponse>>> ENVELOPE_PEDIDO_LIST =
            new ParameterizedTypeReference<>() {};

    private static final ParameterizedTypeReference<ServiceEnvelope<Object>> ENVELOPE_VOID =
            new ParameterizedTypeReference<>() {};

    private final WebClient webClient;

    public PedidoClient(
            @Qualifier("pedidosWebClient")
            WebClient webClient
    ) {
        this.webClient = webClient;
    }

    public PedidoResponse crearPedido(CompraRequest request) {
        log.info("[BFF CLIENT] Creando pedido - Usuario: {}, Productos: {}", 
                request.getUsuarioId(), request.getProductos() != null ? request.getProductos().size() : 0);
        
        try {
            ServiceEnvelope<PedidoResponse> envelope = webClient
                    .post()
                    .uri("/pedidos")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ENVELOPE_PEDIDO)
                    .block();
            
            PedidoResponse response = unwrapPedido(envelope);
            log.info("[BFF CLIENT] Pedido creado exitosamente - ID: {}", response != null ? response.getId() : "null");
            return response;
        } catch (Exception e) {
            log.error("[BFF CLIENT] Error al crear pedido - Usuario: {}, Error: {}", 
                    request.getUsuarioId(), e.getMessage(), e);
            throw e;
        }
    }

    public List<PedidoResponse> listarPedidos() {
        ServiceEnvelope<List<PedidoResponse>> envelope = webClient
                .get()
                .uri("/admin/pedidos")
                .retrieve()
                .bodyToMono(ENVELOPE_PEDIDO_LIST)
                .block();
        return unwrapPedidoList(envelope);
    }

    public List<PedidoResponse> listarTodos() {
        return listarPedidos();
    }

    public List<PedidoResponse> porEstado(String estado) {
        ServiceEnvelope<List<PedidoResponse>> envelope = webClient
                .get()
                .uri("/admin/pedidos/estado/{estado}", estado)
                .retrieve()
                .bodyToMono(ENVELOPE_PEDIDO_LIST)
                .block();
        return unwrapPedidoList(envelope);
    }

    public PedidoResponse preparar(Long pedidoId) {
        return prepararPedido(pedidoId);
    }

    public PedidoResponse enviar(Long pedidoId) {
        return enviarPedido(pedidoId);
    }

    public PedidoResponse entregar(Long pedidoId) {
        return entregarPedido(pedidoId);
    }

    public PedidoResponse obtenerPedido(Long id) {
        ServiceEnvelope<PedidoResponse> envelope = webClient
                .get()
                .uri("/pedidos/{id}", id)
                .retrieve()
                .bodyToMono(ENVELOPE_PEDIDO)
                .block();
        return unwrapPedido(envelope);
    }

    public List<PedidoResponse> pedidosUsuario(String usuarioId) {
        ServiceEnvelope<List<PedidoResponse>> envelope = webClient
                .get()
                .uri("/pedidos/usuario/{usuarioId}", usuarioId)
                .retrieve()
                .bodyToMono(ENVELOPE_PEDIDO_LIST)
                .block();
        return unwrapPedidoList(envelope);
    }

    public PedidoResponse pagarPedido(Long pedidoId) {
        ServiceEnvelope<PedidoResponse> envelope = webClient
                .put()
                .uri("/pedidos/{id}/pagar", pedidoId)
                .retrieve()
                .bodyToMono(ENVELOPE_PEDIDO)
                .block();
        return unwrapPedido(envelope);
    }

    /**
     * El microservicio expone cancelación como DELETE /pedidos/{id}.
     */
    public void cancelarPedido(Long pedidoId) {
        webClient
                .delete()
                .uri("/pedidos/{id}", pedidoId)
                .retrieve()
                .bodyToMono(ENVELOPE_VOID)
                .block();
    }

    public PedidoResponse reactivarPedido(Long pedidoId) {
        ServiceEnvelope<PedidoResponse> envelope = webClient
                .put()
                .uri("/pedidos/{id}/reactivar", pedidoId)
                .retrieve()
                .bodyToMono(ENVELOPE_PEDIDO)
                .block();
        return unwrapPedido(envelope);
    }

    public PedidoResponse prepararPedido(Long pedidoId) {
        ServiceEnvelope<PedidoResponse> envelope = webClient
                .put()
                .uri("/admin/pedidos/{id}/preparar", pedidoId)
                .retrieve()
                .bodyToMono(ENVELOPE_PEDIDO)
                .block();
        return unwrapPedido(envelope);
    }

    public PedidoResponse enviarPedido(Long pedidoId) {
        ServiceEnvelope<PedidoResponse> envelope = webClient
                .put()
                .uri("/admin/pedidos/{id}/enviar", pedidoId)
                .retrieve()
                .bodyToMono(ENVELOPE_PEDIDO)
                .block();
        return unwrapPedido(envelope);
    }

    public PedidoResponse entregarPedido(Long pedidoId) {
        ServiceEnvelope<PedidoResponse> envelope = webClient
                .put()
                .uri("/admin/pedidos/{id}/entregar", pedidoId)
                .retrieve()
                .bodyToMono(ENVELOPE_PEDIDO)
                .block();
        return unwrapPedido(envelope);
    }

    private static PedidoResponse unwrapPedido(ServiceEnvelope<PedidoResponse> envelope) {
        return envelope != null ? envelope.getData() : null;
    }

    private static List<PedidoResponse> unwrapPedidoList(ServiceEnvelope<List<PedidoResponse>> envelope) {
        if (envelope == null || envelope.getData() == null) {
            return List.of();
        }
        return envelope.getData();
    }
}
