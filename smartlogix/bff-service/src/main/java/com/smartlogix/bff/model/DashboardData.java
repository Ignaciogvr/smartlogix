package com.smartlogix.bff.model;

import com.smartlogix.bff.dto.response.EnvioResponse;
import com.smartlogix.bff.dto.response.PedidoResponse;
import com.smartlogix.bff.dto.response.ProductoCatalogoDTO;
import com.smartlogix.bff.dto.response.UsuarioResponse;

import java.util.List;

public class DashboardData {

    // =========================
    // RESUMEN GENERAL
    // =========================

    private Integer totalUsuarios;

    private Integer totalPedidos;

    private Integer pedidosPendientes;

    private Integer pedidosEnviados;

    private Integer pedidosEntregados;

    private Double ventasTotales;

    // =========================
    // DATA RECIENTE
    // =========================

    private List<PedidoResponse> ultimosPedidos;

    private List<UsuarioResponse> ultimosUsuarios;

    private List<ProductoCatalogoDTO> productosPopulares;

    private List<EnvioResponse> enviosRecientes;

    // =========================
    // ALERTAS
    // =========================

    private Integer productosSinStock;

    private Integer enviosRetrasados;

    // =========================
    // CONSTRUCTORES
    // =========================

    public DashboardData() {
    }

    public DashboardData(
            Integer totalUsuarios,
            Integer totalPedidos,
            Integer pedidosPendientes,
            Integer pedidosEnviados,
            Integer pedidosEntregados,
            Double ventasTotales,
            List<PedidoResponse> ultimosPedidos,
            List<UsuarioResponse> ultimosUsuarios,
            List<ProductoCatalogoDTO> productosPopulares,
            List<EnvioResponse> enviosRecientes,
            Integer productosSinStock,
            Integer enviosRetrasados
    ) {
        this.totalUsuarios = totalUsuarios;
        this.totalPedidos = totalPedidos;
        this.pedidosPendientes = pedidosPendientes;
        this.pedidosEnviados = pedidosEnviados;
        this.pedidosEntregados = pedidosEntregados;
        this.ventasTotales = ventasTotales;
        this.ultimosPedidos = ultimosPedidos;
        this.ultimosUsuarios = ultimosUsuarios;
        this.productosPopulares = productosPopulares;
        this.enviosRecientes = enviosRecientes;
        this.productosSinStock = productosSinStock;
        this.enviosRetrasados = enviosRetrasados;
    }

    // =========================
    // GETTERS & SETTERS
    // =========================

    public Integer getTotalUsuarios() {
        return totalUsuarios;
    }

    public void setTotalUsuarios(Integer totalUsuarios) {
        this.totalUsuarios = totalUsuarios;
    }

    public Integer getTotalPedidos() {
        return totalPedidos;
    }

    public void setTotalPedidos(Integer totalPedidos) {
        this.totalPedidos = totalPedidos;
    }

    public Integer getPedidosPendientes() {
        return pedidosPendientes;
    }

    public void setPedidosPendientes(Integer pedidosPendientes) {
        this.pedidosPendientes = pedidosPendientes;
    }

    public Integer getPedidosEnviados() {
        return pedidosEnviados;
    }

    public void setPedidosEnviados(Integer pedidosEnviados) {
        this.pedidosEnviados = pedidosEnviados;
    }

    public Integer getPedidosEntregados() {
        return pedidosEntregados;
    }

    public void setPedidosEntregados(Integer pedidosEntregados) {
        this.pedidosEntregados = pedidosEntregados;
    }

    public Double getVentasTotales() {
        return ventasTotales;
    }

    public void setVentasTotales(Double ventasTotales) {
        this.ventasTotales = ventasTotales;
    }

    public List<PedidoResponse> getUltimosPedidos() {
        return ultimosPedidos;
    }

    public void setUltimosPedidos(List<PedidoResponse> ultimosPedidos) {
        this.ultimosPedidos = ultimosPedidos;
    }

    public List<UsuarioResponse> getUltimosUsuarios() {
        return ultimosUsuarios;
    }

    public void setUltimosUsuarios(List<UsuarioResponse> ultimosUsuarios) {
        this.ultimosUsuarios = ultimosUsuarios;
    }

    public List<ProductoCatalogoDTO> getProductosPopulares() {
        return productosPopulares;
    }

    public void setProductosPopulares(List<ProductoCatalogoDTO> productosPopulares) {
        this.productosPopulares = productosPopulares;
    }

    public List<EnvioResponse> getEnviosRecientes() {
        return enviosRecientes;
    }

    public void setEnviosRecientes(List<EnvioResponse> enviosRecientes) {
        this.enviosRecientes = enviosRecientes;
    }

    public Integer getProductosSinStock() {
        return productosSinStock;
    }

    public void setProductosSinStock(Integer productosSinStock) {
        this.productosSinStock = productosSinStock;
    }

    public Integer getEnviosRetrasados() {
        return enviosRetrasados;
    }

    public void setEnviosRetrasados(Integer enviosRetrasados) {
        this.enviosRetrasados = enviosRetrasados;
    }
}