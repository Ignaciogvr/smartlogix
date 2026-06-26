package com.smartlogix.bff.service.impl;

import com.smartlogix.bff.client.EnvioClient;
import com.smartlogix.bff.client.InventoryClient;
import com.smartlogix.bff.client.PedidoClient;
import com.smartlogix.bff.client.UsuarioClient;
import com.smartlogix.bff.dto.response.DashboardResponse;
import com.smartlogix.bff.dto.response.EnvioResponse;
import com.smartlogix.bff.dto.response.PedidoResponse;
import com.smartlogix.bff.dto.response.ProductoCatalogoDTO;
import com.smartlogix.bff.client.dto.ServiceEnvelope;
import com.smartlogix.bff.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final InventoryClient inventoryClient;
    private final PedidoClient pedidoClient;
    private final UsuarioClient usuarioClient;
    private final EnvioClient envioClient;

    public DashboardServiceImpl(
            InventoryClient inventoryClient,
            PedidoClient pedidoClient,
            UsuarioClient usuarioClient,
            EnvioClient envioClient
    ) {
        this.inventoryClient = inventoryClient;
        this.pedidoClient = pedidoClient;
        this.usuarioClient = usuarioClient;
        this.envioClient = envioClient;
    }

    @Override
    public DashboardResponse dashboardAdmin() {

        List<PedidoResponse> pedidos =
                pedidoClient.listarPedidos();

        List<ProductoCatalogoDTO> productos =
                dataOrEmpty(inventoryClient.productosActivos());

        List<EnvioResponse> envios =
                envioClient.listarEnvios();

        long totalUsuarios =
                usuarioClient.existeUsuario("admin") ? 1L : 0L;

        long pedidosPendientes =
                pedidos.stream()
                        .filter(p -> "PENDIENTE".equalsIgnoreCase(p.getEstado()))
                        .count();

        long pedidosEnviados =
                pedidos.stream()
                        .filter(p -> "ENVIADO".equalsIgnoreCase(p.getEstado())
                                || "ENTREGADO".equalsIgnoreCase(p.getEstado()))
                        .count();

        double ventasTotales =
                pedidos.stream()
                        .mapToDouble(p -> p.getTotal() != null ? p.getTotal() : 0.0)
                        .sum();

        DashboardResponse response = new DashboardResponse();

        response.setTotalUsuarios(totalUsuarios);
        response.setTotalPedidos((long) pedidos.size());
        response.setProductosActivos((long) productos.size());
        response.setPedidosPendientes(pedidosPendientes);
        response.setPedidosEnviados(pedidosEnviados);
        response.setVentasTotales(ventasTotales);
        response.setTotalEnvios((long) envios.size());

        return response;
    }

    @Override
    public DashboardResponse dashboardUsuario(String usuarioId) {

        List<PedidoResponse> pedidos =
                pedidoClient.pedidosUsuario(usuarioId);

        List<EnvioResponse> envios =
                envioClient.enviosUsuario(usuarioId);

        long pendientes =
                pedidos.stream()
                        .filter(p -> "PENDIENTE".equalsIgnoreCase(p.getEstado()))
                        .count();

        long enviados =
                pedidos.stream()
                        .filter(p -> "ENVIADO".equalsIgnoreCase(p.getEstado())
                                || "ENTREGADO".equalsIgnoreCase(p.getEstado()))
                        .count();

        double total =
                pedidos.stream()
                        .mapToDouble(p -> p.getTotal() != null ? p.getTotal() : 0.0)
                        .sum();

        DashboardResponse response = new DashboardResponse();

        response.setTotalPedidos((long) pedidos.size());
        response.setPedidosPendientes(pendientes);
        response.setPedidosEnviados(enviados);
        response.setVentasTotales(total);
        response.setTotalEnvios((long) envios.size());

        return response;
    }

    @Override
    public DashboardResponse dashboardVentas() {

        List<PedidoResponse> pedidos =
                pedidoClient.listarPedidos();

        double totalVentas =
                pedidos.stream()
                        .mapToDouble(p -> p.getTotal() != null ? p.getTotal() : 0.0)
                        .sum();

        long pendientes =
                pedidos.stream()
                        .filter(p -> "PENDIENTE".equalsIgnoreCase(p.getEstado()))
                        .count();

        DashboardResponse response = new DashboardResponse();

        response.setTotalPedidos((long) pedidos.size());
        response.setPedidosPendientes(pendientes);
        response.setVentasTotales(totalVentas);

        return response;
    }

    @Override
    public DashboardResponse dashboardLogistica() {

        List<EnvioResponse> envios =
                envioClient.listarEnvios();

        long pendientes =
                envios.stream()
                        .filter(e -> "PENDIENTE".equalsIgnoreCase(e.getEstado()))
                        .count();

        long entregados =
                envios.stream()
                        .filter(e -> "ENTREGADO".equalsIgnoreCase(e.getEstado()))
                        .count();

        DashboardResponse response = new DashboardResponse();

        response.setPedidosPendientes(pendientes);
        response.setPedidosEnviados(entregados);
        response.setTotalEnvios((long) envios.size());

        return response;
    }

    private static <T> List<T> dataOrEmpty(ServiceEnvelope<List<T>> envelope) {
        if (envelope == null || envelope.getData() == null) {
            return Collections.emptyList();
        }
        return envelope.getData();
    }
}