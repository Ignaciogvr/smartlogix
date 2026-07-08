package com.smartlogix.bff.dto.response;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Vista consolidada del estado de un pedido para el cliente.
 * Combina datos de pedidos-service + envio-service:
 *  - Estado general del pedido
 *  - Ítems comprados (con vendedorId por línea)
 *  - Uno o más envíos (uno por vendedor involucrado)
 *  - Línea de tiempo de seguimiento por envío
 */
public class EstadoCompletoResponse {

    private Long pedidoId;
    private UsuarioResumen usuario;
    private String pedidoEstado;
    private Double total;
    private LocalDateTime fechaPedido;
    private List<ItemPedido> items;
    private List<EnvioResponse> envios;

    public EstadoCompletoResponse() {
    }

    /** Constructor de compatibilidad (usado en PedidoAggregator viejo) */
    public EstadoCompletoResponse(
            Long pedidoId,
            UsuarioResumen usuario,
            String pedidoEstado,
            List<EnvioResponse> envios
    ) {
        this.pedidoId = pedidoId;
        this.usuario = usuario;
        this.pedidoEstado = pedidoEstado;
        this.envios = envios;
    }

    public EstadoCompletoResponse(
            Long pedidoId,
            UsuarioResumen usuario,
            String pedidoEstado,
            Double total,
            LocalDateTime fechaPedido,
            List<ItemPedido> items,
            List<EnvioResponse> envios
    ) {
        this.pedidoId = pedidoId;
        this.usuario = usuario;
        this.pedidoEstado = pedidoEstado;
        this.total = total;
        this.fechaPedido = fechaPedido;
        this.items = items;
        this.envios = envios;
    }

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public UsuarioResumen getUsuario() { return usuario; }
    public void setUsuario(UsuarioResumen usuario) { this.usuario = usuario; }

    public String getPedidoEstado() { return pedidoEstado; }
    public void setPedidoEstado(String pedidoEstado) { this.pedidoEstado = pedidoEstado; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public LocalDateTime getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(LocalDateTime fechaPedido) { this.fechaPedido = fechaPedido; }

    public List<ItemPedido> getItems() { return items; }
    public void setItems(List<ItemPedido> items) { this.items = items; }

    public List<EnvioResponse> getEnvios() { return envios; }
    public void setEnvios(List<EnvioResponse> envios) { this.envios = envios; }

    // =============================
    // INNER CLASSES
    // =============================

    public static class UsuarioResumen {
        private String id;
        private String nombre;

        public UsuarioResumen() {}

        public UsuarioResumen(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
    }

    /**
     * Ítem individual del pedido.
     * Incluye vendedorId para que el frontend pueda agrupar por vendedor.
     */
    public static class ItemPedido {
        private Long productoId;
        private String nombreProducto;
        private String imagen;
        private String descripcionCorta;
        private String categoria;
        private Integer cantidad;
        private Double precioUnitario;
        private Double subtotal;
        private String vendedorId;

        public ItemPedido() {}

        public ItemPedido(Long productoId, String nombreProducto,
                          Integer cantidad, Double precioUnitario, String vendedorId) {
            this.productoId = productoId;
            this.nombreProducto = nombreProducto;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
            this.subtotal = (cantidad != null && precioUnitario != null)
                    ? cantidad * precioUnitario : null;
            this.vendedorId = vendedorId;
        }

        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }

        public String getNombreProducto() { return nombreProducto; }
        public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

        public String getImagen() { return imagen; }
        public void setImagen(String imagen) { this.imagen = imagen; }

        public String getDescripcionCorta() { return descripcionCorta; }
        public void setDescripcionCorta(String descripcionCorta) { this.descripcionCorta = descripcionCorta; }

        public String getCategoria() { return categoria; }
        public void setCategoria(String categoria) { this.categoria = categoria; }

        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

        public Double getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }

        public Double getSubtotal() { return subtotal; }
        public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

        public String getVendedorId() { return vendedorId; }
        public void setVendedorId(String vendedorId) { this.vendedorId = vendedorId; }
    }
}
