package com.smartlogix.pedidos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido_historial")
public class PedidoHistorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_anterior")
    private EstadoPedido estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_nuevo", nullable = false)
    private EstadoPedido estadoNuevo;

    @Column(name = "modificado_por")
    private String modificadoPor;

    @Column(nullable = false)
    private LocalDateTime fecha;

    public PedidoHistorial() {}

    public PedidoHistorial(Pedido pedido, EstadoPedido estadoAnterior, EstadoPedido estadoNuevo, String modificadoPor) {
        this.pedido = pedido;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.modificadoPor = modificadoPor;
        this.fecha = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
    
    public EstadoPedido getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(EstadoPedido estadoAnterior) { this.estadoAnterior = estadoAnterior; }
    
    public EstadoPedido getEstadoNuevo() { return estadoNuevo; }
    public void setEstadoNuevo(EstadoPedido estadoNuevo) { this.estadoNuevo = estadoNuevo; }
    
    public String getModificadoPor() { return modificadoPor; }
    public void setModificadoPor(String modificadoPor) { this.modificadoPor = modificadoPor; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
