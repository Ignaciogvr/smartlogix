package com.smartlogix.envio.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "envios", indexes = {
    @Index(name = "idx_envios_pedido_id", columnList = "pedido_id"),
    @Index(name = "idx_envios_usuario_id", columnList = "usuario_id"),
    @Index(name = "idx_envios_tracking_number", columnList = "tracking_number"),
    @Index(name = "idx_envios_estado", columnList = "estado"),
    @Index(name = "idx_envios_fecha_creacion", columnList = "fecha_creacion DESC")
})
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pedidoId;

    // 🔥 CAMBIO CLAVE
    private String usuarioId;

    private String trackingNumber;

    @Enumerated(EnumType.STRING)
    private EstadoEnvio estado;


    private String direccionDestino;

    @Enumerated(EnumType.STRING)
    private TipoEnvio tipoEnvio;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaEstimadaEntrega;

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public EstadoEnvio getEstado() {
        return estado;
    }

    public void setEstado(EstadoEnvio estado) {
        this.estado = estado;
    }






    


    public String getDireccionDestino() {
        return direccionDestino;
    }

    public void setDireccionDestino(String direccionDestino) {
        this.direccionDestino = direccionDestino;
    }

    public TipoEnvio getTipoEnvio() {
        return tipoEnvio;
    }

    public void setTipoEnvio(TipoEnvio tipoEnvio) {
        this.tipoEnvio = tipoEnvio;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaEstimadaEntrega() {
        return fechaEstimadaEntrega;
    }

    public void setFechaEstimadaEntrega(LocalDateTime fechaEstimadaEntrega) {
        this.fechaEstimadaEntrega = fechaEstimadaEntrega;
    }
}