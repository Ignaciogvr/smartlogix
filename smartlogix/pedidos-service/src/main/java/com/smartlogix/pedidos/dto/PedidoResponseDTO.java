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
}