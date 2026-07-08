package com.smartlogix.pedidos.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos", indexes = {
    @Index(name = "idx_pedidos_usuario_id", columnList = "usuario_id"),
    @Index(name = "idx_pedidos_estado", columnList = "estado"),
    @Index(name = "idx_pedidos_fecha", columnList = "fecha DESC"),
    @Index(name = "idx_pedidos_usuario_id_fecha", columnList = "usuario_id, fecha DESC")
})
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 Auth0 usa STRING
    private String usuarioId;

    private LocalDateTime fecha;

    private Double total;

    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;

    // ✅ detalles del pedido
    @OneToMany(
            mappedBy = "pedido",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<DetallePedido> detalles = new ArrayList<>();

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoHistorial> historial = new ArrayList<>();

    // ✅ CAMPOS NUEVOS PARA LOGÍSTICA
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<Pago> pagos = new ArrayList<>();


    private Boolean requiereLogisticaInversa = false;

    private LocalDateTime fechaCompletado;

    // ✅ CAMPOS DE CHECKOUT
    @Column(length = 500)
    private String direccionEnvio;

    @Column(length = 20)
    private String telefonoContacto;

    @Column(columnDefinition = "TEXT")
    private String notasEntrega;

    // 👤 Notas internas visibles solo para ADMIN/VENDEDOR
    @Column(columnDefinition = "TEXT")
    private String notasInternas;

    @Column(length = 50)
    private String metodoPago;

    @Column(columnDefinition = "TEXT")
    private String motivoCancelacion;

    private LocalDateTime fechaEntrega;

    @PrePersist
    public void prePersist() {
        this.fecha = LocalDateTime.now();
        this.estado = EstadoPedido.PENDIENTE;
    }

    public Long getId() {
        return id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public List<Pago> getPagos() { return pagos; }
    public void setPagos(List<Pago> pagos) { this.pagos = pagos; }

    public List<PedidoHistorial> getHistorial() { return historial; }
    public void setHistorial(List<PedidoHistorial> historial) { this.historial = historial; }


    public Boolean getRequiereLogisticaInversa() { return requiereLogisticaInversa; }
    public void setRequiereLogisticaInversa(Boolean requiereLogisticaInversa) { this.requiereLogisticaInversa = requiereLogisticaInversa; }

    public LocalDateTime getFechaCompletado() { return fechaCompletado; }
    public void setFechaCompletado(LocalDateTime fechaCompletado) { this.fechaCompletado = fechaCompletado; }

    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }

    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String telefonoContacto) { this.telefonoContacto = telefonoContacto; }

    public String getNotasEntrega() { return notasEntrega; }
    public void setNotasEntrega(String notasEntrega) { this.notasEntrega = notasEntrega; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getNotasInternas() { return notasInternas; }
    public void setNotasInternas(String notasInternas) { this.notasInternas = notasInternas; }

    public String getMotivoCancelacion() { return motivoCancelacion; }
    public void setMotivoCancelacion(String motivoCancelacion) { this.motivoCancelacion = motivoCancelacion; }

    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDateTime fechaEntrega) { this.fechaEntrega = fechaEntrega; }
}