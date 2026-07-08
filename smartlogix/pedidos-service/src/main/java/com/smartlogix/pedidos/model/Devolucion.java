package com.smartlogix.pedidos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "devoluciones", indexes = {
    @Index(name = "idx_devoluciones_pedido_id", columnList = "pedido_id"),
    @Index(name = "idx_devoluciones_estado", columnList = "estado_devolucion"),
    @Index(name = "idx_devoluciones_usuario_id", columnList = "usuario_id")
})
public class Devolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detalle_del_pedido_id", nullable = false)
    private DetallePedido detallePedido;

    @Column(name = "usuario_id", nullable = false)
    private String usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_devolucion", nullable = false)
    private EstadoDevolucion estadoDevolucion = EstadoDevolucion.INICIADA;

    @Enumerated(EnumType.STRING)
    @Column(name = "motivo_devolucion", nullable = false)
    private MotivoDevolucion motivoDevolucion;

    @Column(name = "descripcion_problema", columnDefinition = "TEXT")
    private String descripcionProblema;

    @Column(name = "cantidad_devuelta", columnDefinition = "INTEGER DEFAULT 1")
    private Integer cantidadDevuelta = 1;

    @Column(name = "cantidad_aceptada")
    private Integer cantidadAceptada;

    @Column(name = "foto_evidencia_url", length = 500)
    private String fotoEvidenciaUrl;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud;

    @Column(name = "fecha_recepcion")
    private LocalDateTime fechaRecepcion;

    @Column(name = "fecha_inspeccion")
    private LocalDateTime fechaInspeccion;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    @Column(name = "inspeccionado_por")
    private String inspeccionadoPor;

    @Column(name = "observaciones_inspeccion", columnDefinition = "TEXT")
    private String observacionesInspeccion;

    @PrePersist
    public void prePersist() {
        if (this.fechaSolicitud == null) {
            this.fechaSolicitud = LocalDateTime.now();
        }
        if (this.estadoDevolucion == null) {
            this.estadoDevolucion = EstadoDevolucion.INICIADA;
        }
    }

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }

    public DetallePedido getDetallePedido() { return detallePedido; }
    public void setDetallePedido(DetallePedido detallePedido) { this.detallePedido = detallePedido; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public EstadoDevolucion getEstadoDevolucion() { return estadoDevolucion; }
    public void setEstadoDevolucion(EstadoDevolucion estadoDevolucion) { this.estadoDevolucion = estadoDevolucion; }

    public MotivoDevolucion getMotivoDevolucion() { return motivoDevolucion; }
    public void setMotivoDevolucion(MotivoDevolucion motivoDevolucion) { this.motivoDevolucion = motivoDevolucion; }

    public String getDescripcionProblema() { return descripcionProblema; }
    public void setDescripcionProblema(String descripcionProblema) { this.descripcionProblema = descripcionProblema; }

    public Integer getCantidadDevuelta() { return cantidadDevuelta; }
    public void setCantidadDevuelta(Integer cantidadDevuelta) { this.cantidadDevuelta = cantidadDevuelta; }

    public Integer getCantidadAceptada() { return cantidadAceptada; }
    public void setCantidadAceptada(Integer cantidadAceptada) { this.cantidadAceptada = cantidadAceptada; }

    public String getFotoEvidenciaUrl() { return fotoEvidenciaUrl; }
    public void setFotoEvidenciaUrl(String fotoEvidenciaUrl) { this.fotoEvidenciaUrl = fotoEvidenciaUrl; }

    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    public LocalDateTime getFechaRecepcion() { return fechaRecepcion; }
    public void setFechaRecepcion(LocalDateTime fechaRecepcion) { this.fechaRecepcion = fechaRecepcion; }

    public LocalDateTime getFechaInspeccion() { return fechaInspeccion; }
    public void setFechaInspeccion(LocalDateTime fechaInspeccion) { this.fechaInspeccion = fechaInspeccion; }

    public LocalDateTime getFechaResolucion() { return fechaResolucion; }
    public void setFechaResolucion(LocalDateTime fechaResolucion) { this.fechaResolucion = fechaResolucion; }

    public String getInspeccionadoPor() { return inspeccionadoPor; }
    public void setInspeccionadoPor(String inspeccionadoPor) { this.inspeccionadoPor = inspeccionadoPor; }

    public String getObservacionesInspeccion() { return observacionesInspeccion; }
    public void setObservacionesInspeccion(String observacionesInspeccion) { this.observacionesInspeccion = observacionesInspeccion; }
}
