package com.smartlogix.pedidos.dto;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponseDTO {

    private Long id;
    private String usuarioId;
    private List<DetallePedidoDTO> productos;
    private Double total;
    private String estado;
    private LocalDateTime fecha;
    private LocalDateTime fechaCompletado;
    private Boolean requiereLogisticaInversa;
    private String notasInternas;
    private List<PagoDTO> pagos;

    public PedidoResponseDTO(Long id, String usuarioId,
                             List<DetallePedidoDTO> productos,
                             Double total,
                             String estado,
                             LocalDateTime fecha) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.productos = productos;
        this.total = total;
        this.estado = estado;
        this.fecha = fecha;
    }

    public PedidoResponseDTO(Long id, String usuarioId,
                             List<DetallePedidoDTO> productos,
                             Double total,
                             String estado,
                             LocalDateTime fecha,
                             LocalDateTime fechaCompletado,
                             Boolean requiereLogisticaInversa,
                             List<PagoDTO> pagos) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.productos = productos;
        this.total = total;
        this.estado = estado;
        this.fecha = fecha;
        this.fechaCompletado = fechaCompletado;
        this.requiereLogisticaInversa = requiereLogisticaInversa;
        this.pagos = pagos;
    }

    public Long getId() {
        return id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public List<DetallePedidoDTO> getProductos() {
        return productos;
    }

    public Double getTotal() {
        return total;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public LocalDateTime getFechaCompletado() {
        return fechaCompletado;
    }

    public void setFechaCompletado(LocalDateTime fechaCompletado) {
        this.fechaCompletado = fechaCompletado;
    }

    public String getNotasInternas() {
        return notasInternas;
    }

    public Boolean getRequiereLogisticaInversa() {
        return requiereLogisticaInversa;
    }

    public void setRequiereLogisticaInversa(Boolean requiereLogisticaInversa) {
        this.requiereLogisticaInversa = requiereLogisticaInversa;
    }

    public void setNotasInternas(String notasInternas) {
        this.notasInternas = notasInternas;
    }

    public List<PagoDTO> getPagos() {
        return pagos;
    }

    public void setPagos(List<PagoDTO> pagos) {
        this.pagos = pagos;
    }

}