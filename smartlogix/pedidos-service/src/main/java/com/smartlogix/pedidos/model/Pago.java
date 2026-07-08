package com.smartlogix.pedidos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Registro de pagos y transacciones de pedidos
 */
@Entity
@Table(name = "pagos", indexes = {
    @Index(name = "idx_pagos_pedido_id", columnList = "pedido_id"),
    @Index(name = "idx_pagos_usuario_id", columnList = "usuario_id"),
    @Index(name = "idx_pagos_estado", columnList = "estado_pago")
})
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Column(nullable = false)
    private String usuarioId;

    @Column(nullable = false)
    private Double monto;

    private String moneda = "CLP";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;

    @Column(unique = true)
    private String numeroReferencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estadoPago = EstadoPago.PENDIENTE;

    private String proveedorPago;
    private String idTransaccionProveedor;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaTransaccion;

    private LocalDateTime fechaConfirmacion;
    private String motivoRechazo;
    private Integer intentosPago = 0;
    private LocalDateTime fechaProximoIntento;

    public Pago() {}

    public Pago(Pedido pedido, String usuarioId, Double monto, MetodoPago metodoPago) {
        this.pedido = pedido;
        this.usuarioId = usuarioId;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.estadoPago = EstadoPago.PENDIENTE;
    }

    @PrePersist
    public void prePersist() {
        this.fechaTransaccion = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }
    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) { this.metodoPago = metodoPago; }
    public String getNumeroReferencia() { return numeroReferencia; }
    public void setNumeroReferencia(String numeroReferencia) { this.numeroReferencia = numeroReferencia; }
    public EstadoPago getEstadoPago() { return estadoPago; }
    public void setEstadoPago(EstadoPago estadoPago) { this.estadoPago = estadoPago; }
    public String getProveedorPago() { return proveedorPago; }
    public void setProveedorPago(String proveedorPago) { this.proveedorPago = proveedorPago; }
    public String getIdTransaccionProveedor() { return idTransaccionProveedor; }
    public void setIdTransaccionProveedor(String idTransaccionProveedor) { this.idTransaccionProveedor = idTransaccionProveedor; }
    public LocalDateTime getFechaTransaccion() { return fechaTransaccion; }
    public LocalDateTime getFechaConfirmacion() { return fechaConfirmacion; }
    public void setFechaConfirmacion(LocalDateTime fechaConfirmacion) { this.fechaConfirmacion = fechaConfirmacion; }
    public String getMotivoRechazo() { return motivoRechazo; }
    public void setMotivoRechazo(String motivoRechazo) { this.motivoRechazo = motivoRechazo; }
    public Integer getIntentosPago() { return intentosPago; }
    public void setIntentosPago(Integer intentosPago) { this.intentosPago = intentosPago; }
    public LocalDateTime getFechaProximoIntento() { return fechaProximoIntento; }
    public void setFechaProximoIntento(LocalDateTime fechaProximoIntento) { this.fechaProximoIntento = fechaProximoIntento; }
}
