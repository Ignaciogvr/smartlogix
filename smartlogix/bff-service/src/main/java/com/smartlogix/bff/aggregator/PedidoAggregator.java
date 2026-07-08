package com.smartlogix.bff.aggregator;

import com.smartlogix.bff.client.EnvioClient;
import com.smartlogix.bff.client.PedidoClient;
import com.smartlogix.bff.client.UsuarioClient;
import com.smartlogix.bff.dto.response.EnvioResponse;
import com.smartlogix.bff.dto.response.EstadoCompletoResponse;
import com.smartlogix.bff.dto.response.EstadoCompletoResponse.ItemPedido;
import com.smartlogix.bff.dto.response.EstadoCompletoResponse.UsuarioResumen;
import com.smartlogix.bff.dto.response.PedidoResponse;
import com.smartlogix.bff.dto.response.UsuarioResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Orquesta la vista consolidada de un pedido:
 *   1. Obtiene el pedido de pedidos-service
 *   2. Intenta obtener el usuario de usuarios-service (opcional)
 *   3. Obtiene todos los envíos asociados de envio-service por pedidoId
 *   4. Construye los items del pedido con vendedorId por línea
 *
 * El cliente puede acceder al seguimiento completo con solo saber su pedidoId.
 */
@Component
public class PedidoAggregator {

    private static final Logger log = LoggerFactory.getLogger(PedidoAggregator.class);

    private final PedidoClient pedidoClient;
    private final EnvioClient envioClient;
    private final UsuarioClient usuarioClient;
    private final com.smartlogix.bff.client.InventoryClient inventoryClient;

    public PedidoAggregator(
            PedidoClient pedidoClient,
            EnvioClient envioClient,
            UsuarioClient usuarioClient,
            com.smartlogix.bff.client.InventoryClient inventoryClient
    ) {
        this.pedidoClient = pedidoClient;
        this.envioClient = envioClient;
        this.usuarioClient = usuarioClient;
        this.inventoryClient = inventoryClient;
    }

    public List<PedidoResponse> listarPedidos() {
        return pedidoClient.listarPedidos();
    }

    public List<PedidoResponse> pedidosPorUsuario(String usuarioId) {
        return pedidoClient.pedidosUsuario(usuarioId);
    }

    public PedidoResponse obtener(Long id) {
        return pedidoClient.obtenerPedido(id);
    }

    /**
     * Vista consolidada para el cliente.
     * Combina pedido + usuario + envíos por pedidoId (uno por vendedor).
     */
    public EstadoCompletoResponse obtenerEstadoCompleto(Long pedidoId) {
        log.info("[AGGREGATOR] obtenerEstadoCompleto pedidoId={}", pedidoId);

        // ── 1. Pedido (crítico) ─────────────────────────────────────────────
        PedidoResponse pedido;
        try {
            pedido = pedidoClient.obtenerPedido(pedidoId);
            if (pedido == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado: " + pedidoId);
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (org.springframework.web.reactive.function.client.WebClientResponseException e) {
            // Propagar el código real del microservicio (ej: 404)
            throw e;
        } catch (Exception e) {
            log.error("[AGGREGATOR] pedidos-service no disponible", e);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "pedidos-service no disponible", e);
        }

        // ── 2. Usuario (opcional) ────────────────────────────────────────────
        UsuarioResumen usuarioResumen = null;
        if (pedido.getUsuarioId() != null) {
            try {
                UsuarioResponse usuario = usuarioClient.obtenerUsuario(pedido.getUsuarioId());
                if (usuario != null) {
                    usuarioResumen = new UsuarioResumen(
                            usuario.getAuth0Id() != null ? usuario.getAuth0Id() : pedido.getUsuarioId(),
                            usuario.getNombre()
                    );
                }
            } catch (Exception e) {
                log.warn("[AGGREGATOR] usuarios-service no disponible, continuando sin datos de usuario", e);
            }
        }

        // ── 3. Envíos por pedidoId (opcional, uno por vendedor) ─────────────
        List<EnvioResponse> envios = Collections.emptyList();
        try {
            List<EnvioResponse> raw = envioClient.enviosPorPedido(pedidoId);
            if (raw != null) {
                envios = raw;
            }
            log.info("[AGGREGATOR] {} envío(s) encontrado(s) para pedidoId={}", envios.size(), pedidoId);
        } catch (Exception e) {
            log.warn("[AGGREGATOR] envio-service no disponible para pedidoId={}", pedidoId, e);
        }

        // ── 4. Items del pedido (con vendedorId por línea y detalles del producto) ──
        List<ItemPedido> items = Collections.emptyList();
        if (pedido.getProductos() != null) {
            items = pedido.getProductos().stream()
                    .map(d -> {
                        ItemPedido item = new ItemPedido(
                                d.getProductoId(),
                                d.getNombreProducto(),
                                d.getCantidad(),
                                d.getPrecioUnitario(),
                                d.getVendedorId()
                        );
                        // Enriquecer con datos del inventario
                        try {
                            var envelope = inventoryClient.obtenerProducto(d.getProductoId());
                            if (envelope != null && envelope.getData() != null) {
                                var producto = envelope.getData();
                                item.setImagen(producto.getImagenPrincipal());
                                item.setDescripcionCorta(producto.getDescripcionCorta());
                                item.setCategoria(producto.getCategoria());
                            }
                        } catch (Exception e) {
                            log.warn("[AGGREGATOR] No se pudo obtener detalles del producto {} desde el inventario", d.getProductoId(), e);
                        }
                        return item;
                    })
                    .collect(Collectors.toList());
        }

        return new EstadoCompletoResponse(
                pedido.getId(),
                usuarioResumen,
                pedido.getEstado(),
                pedido.getTotal(),
                pedido.getFecha(),
                items,
                envios
        );
    }
}