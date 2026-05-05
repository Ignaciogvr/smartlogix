package com.smartlogix.pedidos.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public class PedidoDTO {

    @NotNull(message = "Usuario obligatorio")
    private String usuarioId;

    @NotEmpty(message = "Debe tener productos")
    private List<DetalleDTO> detalles;

    public static class DetalleDTO {

        @NotNull(message = "Producto obligatorio")
        private Long productoId;

        @NotNull
        @Min(value = 1, message = "Cantidad inválida")
        private Integer cantidad;

        @NotNull
        @Min(value = 1, message = "Precio inválido")
        private Double precio;

        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }

        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

        public Double getPrecio() { return precio; }
        public void setPrecio(Double precio) { this.precio = precio; }
    }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public List<DetalleDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleDTO> detalles) { this.detalles = detalles; }
}