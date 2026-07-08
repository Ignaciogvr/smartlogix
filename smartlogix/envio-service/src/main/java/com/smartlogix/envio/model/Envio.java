package com.smartlogix.envio.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "chofer_id")
    private String choferId;

    @Column(name = "chofer_nombre")
    private String choferNombre;

    @Column(name = "direccion_destino", nullable = false)
    private String direccionDestino;

    @Enumerated(EnumType.STRING)
    private TipoEnvio tipoEnvio;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    private LocalDateTime fechaEstimadaEntrega;

    @Column(name = "foto_entrega_url")
    private String fotoEntregaUrl;

    @Column(name = "firma_receptor_url")
    private String firmaReceptorUrl;

    @OneToMany(mappedBy = "envio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistorialEnvio> historial = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) { this.id = id; }

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
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public LocalDateTime getFechaEstimadaEntrega() {
        return fechaEstimadaEntrega;
    }

    public void setFechaEstimadaEntrega(LocalDateTime fechaEstimadaEntrega) {
        this.fechaEstimadaEntrega = fechaEstimadaEntrega;
    }

    public List<HistorialEnvio> getHistorial() { return historial; }
    public void setHistorial(List<HistorialEnvio> historial) { this.historial = historial; }

    public String getChoferId() { return choferId; }
    public void setChoferId(String choferId) { this.choferId = choferId; }

    public String getChoferNombre() { return choferNombre; }
    public void setChoferNombre(String choferNombre) { this.choferNombre = choferNombre; }

    public String getFotoEntregaUrl() { return fotoEntregaUrl; }
    public void setFotoEntregaUrl(String fotoEntregaUrl) { this.fotoEntregaUrl = fotoEntregaUrl; }

    public String getFirmaReceptorUrl() { return firmaReceptorUrl; }
    public void setFirmaReceptorUrl(String firmaReceptorUrl) { this.firmaReceptorUrl = firmaReceptorUrl; }
}