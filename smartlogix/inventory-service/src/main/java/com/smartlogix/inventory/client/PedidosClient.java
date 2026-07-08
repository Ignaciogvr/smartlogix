package com.smartlogix.inventory.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Cliente HTTP para comunicarse con pedidos-service.
 * Valida si un usuario tiene una compra entregada de un producto dado.
 * Usa RestTemplate simple (sin Feign) para evitar dependencias adicionales.
 */
@Component
public class PedidosClient {

    private static final Logger log = LoggerFactory.getLogger(PedidosClient.class);

    private final RestTemplate restTemplate;

    @Value("${pedidos.service.url:http://pedidos-service:8082}")
    private String pedidosServiceUrl;

    public PedidosClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Verifica si el usuario tiene un pedido DELIVERED con ese producto.
     * En caso de error de comunicación, retorna false (fail-safe: no permite comentar).
     */
    public boolean verificarCompra(String usuarioId, Long productoId) {
        try {
            String url = UriComponentsBuilder
                .fromHttpUrl(pedidosServiceUrl + "/internal/pedidos/verificar-compra")
                .queryParam("usuarioId", usuarioId)
                .queryParam("productoId", productoId)
                .toUriString();

            Boolean result = restTemplate.getForObject(url, Boolean.class);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.warn("[PedidosClient] No se pudo verificar la compra para usuario={}, producto={}: {}",
                    usuarioId, productoId, e.getMessage());
            // Fail-open: si el servicio no responde, permite comentar sin verificación
            // Cambia a 'return false' para política estricta
            return false;
        }
    }
}
