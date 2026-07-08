package com.smartlogix.inventory.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos_proveedor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoProveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    @Column(name = "numero_pedido", unique = true, length = 100)
    private String numeroPedido;

    @Column(name = "cantidad_total")
    private Integer cantidadTotal;

    @Column(name = "monto_total")
    private Double montoTotal;

    @Column(name = "estado_pedido", length = 50)
    private String estadoPedido;

    @Column(name = "fecha_solicitud")
    private LocalDateTime fechaSolicitud;

    @Column(name = "fecha_entrega_esperada")
    private LocalDateTime fechaEntregaEsperada;

    @Column(name = "fecha_entrega_real")
    private LocalDateTime fechaEntregaReal;

    @Column(name = "numero_seguimiento", length = 255)
    private String numeroSeguimiento;
    
    @OneToMany(mappedBy = "pedidoProveedor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetallePedidoProveedor> detalles;
}
