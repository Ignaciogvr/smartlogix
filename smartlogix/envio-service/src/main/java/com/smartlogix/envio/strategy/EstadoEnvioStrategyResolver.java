package com.smartlogix.envio.strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * Strategy Pattern: resuelve qué EstadoEnvioStrategy aplicar
 * según el estado del pedido recibido por Kafka.
 *
 * Reemplaza el switch/case que estaba en el consumer.
 */
public class EstadoEnvioStrategyResolver {

    private static final Map<String, EstadoEnvioStrategy> STRATEGIES = new HashMap<>();

    static {
        // Mapeo: estado pedido → strategy de envío
        STRATEGIES.put("PENDIENTE", new PendienteStrategy());
        STRATEGIES.put("PAGADO", new PreparandoEnvioStrategy());
        STRATEGIES.put("EN_PREPARACION", new PreparandoEnvioStrategy());
        STRATEGIES.put("ENVIADO", new EnRutaStrategy());
        STRATEGIES.put("ENTREGADO", new EntregadoStrategy());
        STRATEGIES.put("CANCELADO", new CanceladoStrategy());
        STRATEGIES.put("EXPIRADO", new CanceladoStrategy());
    }

    /**
     * Devuelve la strategy correspondiente al estado del pedido.
     * @param estadoPedido estado recibido desde pedidos-service
     * @return strategy o null si no hay mapeo
     */
    public static EstadoEnvioStrategy resolver(String estadoPedido) {
        return STRATEGIES.get(estadoPedido);
    }
}
