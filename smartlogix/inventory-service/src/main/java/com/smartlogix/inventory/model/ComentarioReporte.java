package com.smartlogix.inventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Registro de denuncias/reportes sobre comentarios de productos.
 * Persiste en comentario_reportes (creado por V23__add_comentarios_features.sql).
 */
@Entity
@Table(name = "comentario_reportes",
        indexes = @Index(name = "idx_reporte_comentario", columnList = "comentario_id"))
public class ComentarioReporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comentario_id", nullable = false)
    private ProductoComentario comentario;

    @Column(name = "usuario_id", nullable = false)
    private String usuarioId;

    @Column(nullable = false, length = 500)
    private String motivo;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private Boolean resuelto = false;

    @PrePersist
    protected void onCreate() {
        if (this.fecha == null) this.fecha = LocalDateTime.now();
    }

    public ComentarioReporte() {}

    public Long getId() { return id; }
    public ProductoComentario getComentario() { return comentario; }
    public String getUsuarioId() { return usuarioId; }
    public String getMotivo() { return motivo; }
    public LocalDateTime getFecha() { return fecha; }
    public Boolean getResuelto() { return resuelto; }

    public void setId(Long id) { this.id = id; }
    public void setComentario(ProductoComentario comentario) { this.comentario = comentario; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public void setResuelto(Boolean resuelto) { this.resuelto = resuelto; }
}
