package com.smartlogix.pedidos.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CarritoResponse {
    private Long id;
    private String usuarioId;
    private String estado;
    private Double total;
    private LocalDateTime fechaActualizacion;
    private List<CarritoItemResponse> items;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    public List<CarritoItemResponse> getItems() { return items; }
    public void setItems(List<CarritoItemResponse> items) { this.items = items; }
}
