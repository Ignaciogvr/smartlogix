package com.smartlogix.bff.client;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class InventoryClient {

    private final WebClient webClient;

    public InventoryClient(
            @Qualifier("inventoryWebClient")
            WebClient webClient
    ) {
        this.webClient = webClient;
    }

    // 🔥 LISTAR PRODUCTOS
    public Object listarProductos() {
        return webClient
                .get()
                .uri("/productos")
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }

    // 🔥 PRODUCTO POR ID
    public Object obtenerProducto(Long id) {
        return webClient
                .get()
                .uri("/productos/" + id)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }

    // 🔥 ACTIVOS
    public Object productosActivos() {
        return webClient
                .get()
                .uri("/productos/activos")
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }

    // 🔥 POR CATEGORIA
    public Object porCategoria(String categoria) {
        return webClient
                .get()
                .uri("/productos/categoria/" + categoria)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }
}