package com.smartlogix.bff.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class InventoryClient {

    private final WebClient inventoryWebClient;

    public InventoryClient(WebClient inventoryWebClient) {
        this.inventoryWebClient = inventoryWebClient;
    }

    public Object listarProductos() {
        return inventoryWebClient.get()
                .uri("/productos")
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }

    public Object obtenerProducto(Long id) {
        return inventoryWebClient.get()
                .uri("/productos/" + id)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }

    public Object productosActivos() {
        return inventoryWebClient.get()
                .uri("/productos/activos")
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }

    public Object porCategoria(String categoria) {
        return inventoryWebClient.get()
                .uri("/productos/categoria/" + categoria)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }

    // 🔥 NUEVO: destacados
    public Object destacados() {
        return inventoryWebClient.get()
                .uri("/productos/destacados")
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }

    // 🔥 NUEVO: stock
    public Object stock(Long id) {
        return inventoryWebClient.get()
                .uri("/productos/stock/" + id)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }
}