package com.smartlogix.pedidos.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "detalle_pedido", indexes = {
    @Index(name = "idx_detalle_pedido_pedido_id", columnList = "pedido_id"),
    @Index(name = "idx_detalle_pedido_producto_id", columnList = "producto_id")
})
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "producto_id")
    private Long productoId;

    private Integer cantidad;

    private Double precio;

    @Column(name = "vendedor_id")
    private String vendedorId;

    /** Snapshot fields: frozen at purchase time */
    @Column(name = "nombre_producto", length = 255)
    private String nombreProducto;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(name = "descripcion_snapshot", columnDefinition = "TEXT")
    private String descripcionSnapshot;

    @Column(name = "marca_snapshot", length = 100)
    private String marcaSnapshot;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    @JsonBackReference
    private Pedido pedido;

    public DetallePedido() {
    }

    public Long getId() {
        return id;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public String getVendedorId() { return vendedorId; }
    public void setVendedorId(String vendedorId) { this.vendedorId = vendedorId; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public String getDescripcionSnapshot() { return descripcionSnapshot; }
    public void setDescripcionSnapshot(String descripcionSnapshot) { this.descripcionSnapshot = descripcionSnapshot; }

    public String getMarcaSnapshot() { return marcaSnapshot; }
    public void setMarcaSnapshot(String marcaSnapshot) { this.marcaSnapshot = marcaSnapshot; }
}