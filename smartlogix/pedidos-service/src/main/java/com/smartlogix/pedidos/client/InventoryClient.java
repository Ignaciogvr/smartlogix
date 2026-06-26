package com.smartlogix.pedidos.client;

import com.smartlogix.pedidos.exception.ApiResponse;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class InventoryClient {

    private static final Logger log = LoggerFactory.getLogger(InventoryClient.class);

    private final WebClient webClient;
    private final CircuitBreaker circuitBreaker;

    public InventoryClient(WebClient.Builder builder,
                           CircuitBreakerRegistry registry) {

        this.webClient = builder
                .baseUrl("http://inventory-service:8081")
                .build();

        this.circuitBreaker = registry.circuitBreaker("inventory-service");
    }

    public Integer obtenerStock(Long productoId) {

        log.info("[INVENTORY] Consultando stock para productoID: {}", productoId);

        try {
            ApiResponse response = circuitBreaker.executeSupplier(() ->

                    webClient.get()
                            .uri("/productos/stock/{id}", productoId)
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<ApiResponse>() {})
                            .block()
            );

            if (response == null || response.getData() == null) {
                log.error("[INVENTORY] Error: Respuesta nula al obtener stock para productoID: {}", productoId);
                throw new IllegalStateException("No se pudo obtener stock del producto");
            }

            Integer stock = (Integer) response.getData();
            log.info("[INVENTORY] Stock obtenido para productoID: {}, Stock: {}", productoId, stock);
            return stock;

        } catch (Exception e) {
            log.error("[INVENTORY] Error al obtener stock para productoID: {}, Error: {}", productoId, e.getMessage(), e);
            throw new IllegalStateException("Error al comunicarse con inventory-service: " + e.getMessage(), e);
        }
    }

    public void reponerStock(Long productoId, Integer cantidad) {

        log.info("[INVENTORY] Reposición de stock - ProductoID: {}, Cantidad: {}", productoId, cantidad);

        try {
            circuitBreaker.executeRunnable(() ->

                    webClient.put()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/productos/stock/{id}/reponer")
                                    .queryParam("cantidad", cantidad)
                                    .build(productoId))
                            .retrieve()
                            .bodyToMono(new ParameterizedTypeReference<ApiResponse>() {})
                            .block()
            );

            log.info("[INVENTORY] Stock repuesto correctamente - ProductoID: {}, Cantidad: {}", productoId, cantidad);

        } catch (Exception e) {
            log.error("[INVENTORY] Error al reponer stock - ProductoID: {}, Cantidad: {}, Error: {}", 
                    productoId, cantidad, e.getMessage(), e);
            throw new IllegalStateException("Error al reponer stock: " + e.getMessage(), e);
        }
    }
}
