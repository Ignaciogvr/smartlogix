package com.smartlogix.envio.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class PedidoClient {

    private static final Logger log = LoggerFactory.getLogger(PedidoClient.class);

    private final WebClient webClient;

    public PedidoClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://pedidos-service:8082")
                .build();
    }

    public String obtenerPedido(String pedidoId) {

        log.info("📡 Pedido id={}", pedidoId);

        return webClient.get()
                .uri("/internal/pedidos/{id}", pedidoId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}