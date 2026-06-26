package com.smartlogix.bff.dto.response;

public class DashboardResponse {

    private Long totalUsuarios;
    private Long totalPedidos;
    private Long pedidosPendientes;
    private Long pedidosEnviados;
    private Double ventasTotales;
    private Long productosActivos;

    // 🔥 NUEVO CAMPO (te faltaba esto)
    private Long totalEnvios;

    public DashboardResponse() {
    }

    public Long getTotalUsuarios() {
        return totalUsuarios;
    }

    public void setTotalUsuarios(Long totalUsuarios) {
        this.totalUsuarios = totalUsuarios;
    }

    public Long getTotalPedidos() {
        return totalPedidos;
    }

    public void setTotalPedidos(Long totalPedidos) {
        this.totalPedidos = totalPedidos;
    }

    public Long getPedidosPendientes() {
        return pedidosPendientes;
    }

    public void setPedidosPendientes(Long pedidosPendientes) {
        this.pedidosPendientes = pedidosPendientes;
    }

    public Long getPedidosEnviados() {
        return pedidosEnviados;
    }

    public void setPedidosEnviados(Long pedidosEnviados) {
        this.pedidosEnviados = pedidosEnviados;
    }

    public Double getVentasTotales() {
        return ventasTotales;
    }

    public void setVentasTotales(Double ventasTotales) {
        this.ventasTotales = ventasTotales;
    }

    public Long getProductosActivos() {
        return productosActivos;
    }

    public void setProductosActivos(Long productosActivos) {
        this.productosActivos = productosActivos;
    }

    // =========================
    // ENVÍOS
    // =========================

    public Long getTotalEnvios() {
        return totalEnvios;
    }

    public void setTotalEnvios(Long totalEnvios) {
        this.totalEnvios = totalEnvios;
    }
}