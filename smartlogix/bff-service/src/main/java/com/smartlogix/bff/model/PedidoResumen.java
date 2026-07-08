package com.smartlogix.bff.model;

import java.time.LocalDateTime;

public class PedidoResumen {

    // =========================
    // PEDIDO
    // =========================

    private Long pedidoId;

    private String usuarioId;

    private String estado;

    private Double total;

    private Integer cantidadProductos;

    // =========================
    // ENVIO
    // =========================

    private String trackingCode;

    private String estadoEnvio;

    // =========================
    // FECHAS
    // =========================

    private LocalDateTime fechaPedido;

    private LocalDateTime fechaPago;

    private LocalDateTime fechaEntrega;

    // =========================
    // CONSTRUCTORES
    // =========================

    public PedidoResumen() {
    }

    public PedidoResumen(
            Long pedidoId,
            String usuarioId,
            String estado,
            Double total,
            Integer cantidadProductos,
            String trackingCode,
            String estadoEnvio,
            LocalDateTime fechaPedido,
            LocalDateTime fechaPago,
            LocalDateTime fechaEntrega
    ) {
        this.pedidoId = pedidoId;
        this.usuarioId = usuarioId;
        this.estado = estado;
        this.total = total;
        this.cantidadProductos = cantidadProductos;
        this.trackingCode = trackingCode;
        this.estadoEnvio = estadoEnvio;
        this.fechaPedido = fechaPedido;
        this.fechaPago = fechaPago;
        this.fechaEntrega = fechaEntrega;
    }

    // =========================
    // GETTERS & SETTERS
    // =========================

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getCantidadProductos() {
        return cantidadProductos;
    }

    public void setCantidadProductos(Integer cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public String getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(String estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }

    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }
}