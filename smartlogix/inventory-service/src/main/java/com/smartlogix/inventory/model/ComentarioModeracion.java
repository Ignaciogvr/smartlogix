package com.smartlogix.inventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comentario_moderacion")
public class ComentarioModeracion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comentario_id", nullable = false, unique = true)
    private Long comentarioId;

    @Column(name = "moderador_id", nullable = false)
    private Long moderadorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "accion", nullable = false)
    private ModeracionAccion accion;

    @Column(name = "fecha_accion", nullable = false)
    private LocalDateTime fechaAccion;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getComentarioId() { return comentarioId; }
    public void setComentarioId(Long comentarioId) { this.comentarioId = comentarioId; }
    public Long getModeradorId() { return moderadorId; }
    public void setModeradorId(Long moderadorId) { this.moderadorId = moderadorId; }
    public ModeracionAccion getAccion() { return accion; }
    public void setAccion(ModeracionAccion accion) { this.accion = accion; }
    public LocalDateTime getFechaAccion() { return fechaAccion; }
    public void setFechaAccion(LocalDateTime fechaAccion) { this.fechaAccion = fechaAccion; }
}
