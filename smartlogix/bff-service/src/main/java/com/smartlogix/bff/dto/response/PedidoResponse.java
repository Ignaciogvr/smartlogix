package com.smartlogix.bff.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponse {

    private Long id;

    private String usuarioId;

    private List<DetallePedidoResponse> productos;

    private Double total;

    private String estado;

    private LocalDateTime fecha;

    public PedidoResponse() {
    }

    public PedidoResponse(
            Long id,
            String usuarioId,
            List<DetallePedidoResponse> productos,
            Double total,
            String estado,
            LocalDateTime fecha
    ) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.productos = productos;
        this.total = total;
        this.estado = estado;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<DetallePedidoResponse> getProductos() {
        return productos;
    }

    public void setProductos(
            List<DetallePedidoResponse> productos
    ) {
        this.productos = productos;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}