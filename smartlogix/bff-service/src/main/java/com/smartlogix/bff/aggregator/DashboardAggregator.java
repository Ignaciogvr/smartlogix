package com.smartlogix.bff.aggregator;

import com.smartlogix.bff.client.EnvioClient;
import com.smartlogix.bff.client.InventoryClient;
import com.smartlogix.bff.client.PedidoClient;
import com.smartlogix.bff.client.UsuarioClient;
import com.smartlogix.bff.dto.response.DashboardResponse;
import com.smartlogix.bff.dto.response.EnvioResponse;
import com.smartlogix.bff.dto.response.PedidoResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DashboardAggregator {

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

        // =========================
        // PEDIDOS
        // =========================
        List<PedidoResponse> pedidos = pedidoClient.listarPedidos();

        res.setTotalPedidos((long) pedidos.size());

        res.setVentasTotales(
                pedidos.stream()
                        .mapToDouble(p -> p.getTotal() != null ? p.getTotal() : 0.0)
                        .sum()
        );

        // =========================
        // USUARIOS
        // =========================
        res.setTotalUsuarios(
                usuarioClient.existeUsuario("dummy") ? 1L : 0L
        );

        // =========================
        // ENVIOS
        // =========================
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

        // =========================
        // PRODUCTOS
        // =========================
        var productosResponse = inventoryClient.listarProductos();

        res.setProductosActivos(
                productosResponse != null && productosResponse.getData() != null
                        ? (long) productosResponse.getData().size()
                        : 0L
        );

        return res;
    }
}