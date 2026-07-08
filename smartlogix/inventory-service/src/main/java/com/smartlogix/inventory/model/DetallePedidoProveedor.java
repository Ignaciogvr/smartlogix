package com.smartlogix.inventory.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalles_pedido_proveedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetallePedidoProveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_proveedor_id", nullable = false)
    private PedidoProveedor pedidoProveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Column(name = "cantidad_solicitada")
    private Integer cantidadSolicitada;

    @Column(name = "cantidad_recibida")
    private Integer cantidadRecibida;

    @Column(name = "precio_unitario")
    private Double precioUnitario;
}
