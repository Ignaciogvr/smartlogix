package com.smartlogix.bff.aggregator;

import com.smartlogix.bff.client.EnvioClient;
import com.smartlogix.bff.client.InventoryClient;
import com.smartlogix.bff.client.PedidoClient;
import com.smartlogix.bff.client.UsuarioClient;
import com.smartlogix.bff.dto.response.DashboardResponse;
import com.smartlogix.bff.dto.response.EnvioResponse;
import com.smartlogix.bff.dto.response.PedidoResponse;
import com.smartlogix.bff.dto.response.UsuarioResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DashboardAggregator {

    private static final Logger log = LoggerFactory.getLogger(DashboardAggregator.class);

    private final PedidoClient pedidoClient;
    private final UsuarioClient usuarioClient;
    private final EnvioClient envioClient;
    private final InventoryClient inventoryClient;

    public DashboardAggregator(
            PedidoClient pedidoClient,
            UsuarioClient usuarioClient,
            EnvioClient envioClient,
            InventoryClient inventoryClient
    ) {
        this.pedidoClient = pedidoClient;
        this.usuarioClient = usuarioClient;
        this.envioClient = envioClient;
        this.inventoryClient = inventoryClient;
    }

    public DashboardResponse buildAdminDashboard() {
        DashboardResponse res = new DashboardResponse();

        try {
            List<PedidoResponse> pedidos = pedidoClient.listarPedidos();
            res.setTotalPedidos((long) pedidos.size());
            res.setVentasTotales(
                    pedidos.stream()
                            .mapToDouble(p -> p.getTotal() != null ? p.getTotal() : 0.0)
                            .sum()
            );
        } catch (Exception e) {
            log.error("Error obteniendo pedidos para dashboard admin", e);
            throw new RuntimeException("Error al obtener pedidos del microservicio", e);
        }

        try {
            List<UsuarioResponse> usuarios = usuarioClient.listarUsuarios();
            res.setTotalUsuarios(
                    usuarios != null ? (long) usuarios.size() : 0L
            );
        } catch (Exception e) {
            log.error("Error obteniendo usuarios para dashboard admin", e);
            throw new RuntimeException("Error al obtener usuarios del microservicio", e);
        }

        try {
            List<EnvioResponse> envios = envioClient.listarEnvios();
            res.setPedidosEnviados(
                    envios.stream()
                            .filter(e -> "ENTREGADO".equalsIgnoreCase(e.getEstado()))
                            .count()
            );
            res.setPedidosPendientes(
                    envios.stream()
                            .filter(e -> !"ENTREGADO".equalsIgnoreCase(e.getEstado()))
                            .count()
            );
        } catch (Exception e) {
            log.error("Error obteniendo envíos para dashboard admin", e);
            throw new RuntimeException("Error al obtener envíos del microservicio", e);
        }

        try {
            var productosResponse = inventoryClient.listarProductos();
            res.setProductosActivos(
                    productosResponse != null && productosResponse.getData() != null
                            ? (long) productosResponse.getData().size()
                            : 0L
            );
        } catch (Exception e) {
            log.error("Error obteniendo productos para dashboard admin", e);
            throw new RuntimeException("Error al obtener productos del microservicio", e);
        }

        return res;
    }
}