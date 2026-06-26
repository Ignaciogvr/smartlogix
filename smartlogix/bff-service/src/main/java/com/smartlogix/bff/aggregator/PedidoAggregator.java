package com.smartlogix.bff.aggregator;

import com.smartlogix.bff.client.PedidoClient;
import com.smartlogix.bff.client.EnvioClient;
import com.smartlogix.bff.client.UsuarioClient;
import com.smartlogix.bff.dto.response.PedidoResponse;
import com.smartlogix.bff.dto.response.EnvioResponse;
import com.smartlogix.bff.dto.response.UsuarioResponse;
import com.smartlogix.bff.dto.response.EstadoCompletoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class PedidoAggregator {

    private static final Logger log = LoggerFactory.getLogger(PedidoAggregator.class);

    private final PedidoClient pedidoClient;
    private final EnvioClient envioClient;
    private final UsuarioClient usuarioClient;

    public PedidoAggregator(
            PedidoClient pedidoClient,
            EnvioClient envioClient,
            UsuarioClient usuarioClient
    ) {
        this.pedidoClient = pedidoClient;
        this.envioClient = envioClient;
        this.usuarioClient = usuarioClient;
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
     * Une pedido de PedidoClient y envío de EnvioClient de forma simple.
     */
    public EstadoCompletoResponse obtenerEstadoCompleto(Long pedidoId) {
        log.info("PedidoAggregator: obtenerEstadoCompleto para pedidoId={}", pedidoId);

        // 1. Obtener pedido (Crítico)
        PedidoResponse pedido;
        try {
            pedido = pedidoClient.obtenerPedido(pedidoId);
            if (pedido == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado");
            }
        } catch (Exception e) {
            log.error("Error al obtener pedido", e);
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "pedidos-service no disponible", e);
        }

        // 2. Obtener usuario (Opcional)
        EstadoCompletoResponse.UsuarioResumen usuarioResumen = null;
        if (pedido.getUsuarioId() != null) {
            try {
                UsuarioResponse usuario = usuarioClient.obtenerUsuario(pedido.getUsuarioId());
                if (usuario != null) {
                    usuarioResumen = new EstadoCompletoResponse.UsuarioResumen(
                            usuario.getAuth0Id() != null ? usuario.getAuth0Id() : pedido.getUsuarioId(),
                            usuario.getNombre()
                    );
                }
            } catch (Exception e) {
                log.warn("No se pudo obtener usuario de usuarios-service", e);
            }
        }

        // 3. Obtener envío (Opcional)
        String envioEstado = null;
        String trackingNumber = null;
        boolean envioDisponible = false;

        try {
            List<EnvioResponse> envios = envioClient.enviosPorPedido(pedidoId);
            if (envios != null && !envios.isEmpty()) {
                EnvioResponse envio = envios.get(0);
                envioEstado = envio.getEstado();
                trackingNumber = envio.getTrackingCode();
                envioDisponible = true;
            }
        } catch (Exception e) {
            log.warn("No se pudo obtener envío de envio-service", e);
        }

        return new EstadoCompletoResponse(
                pedido.getId(),
                usuarioResumen,
                pedido.getEstado(),
                envioEstado,
                trackingNumber,
                envioDisponible
        );
    }
}