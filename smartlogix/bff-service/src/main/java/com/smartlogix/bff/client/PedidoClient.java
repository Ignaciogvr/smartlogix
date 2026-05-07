package com.smartlogix.bff.client;

import com.smartlogix.bff.dto.CompraRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class PedidoClient {

    private final WebClient webClient;

    public PedidoClient(
            @Qualifier("pedidosWebClient")
            WebClient webClient
    ) {
        this.webClient = webClient;
    }

    // 🔥 CREAR PEDIDO
    public Object crearPedido(CompraRequest request) {

        return webClient
                .post()
                .uri("/pedidos")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }

    // 🔥 LISTAR
    public Object listarPedidos() {
        return webClient
                .get()
                .uri("/pedidos")
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }

    // 🔥 OBTENER
    public Object obtenerPedido(Long id) {
        return webClient
                .get()
                .uri("/pedidos/" + id)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }
}