package com.smartlogix.pedidos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos_analytics_eventos")
public class AnalyticsEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tipo_evento", nullable = false)
    private String tipoEvento;
    
    @Column(columnDefinition = "TEXT")
    private String payload;
    
    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;
    
    @PrePersist
    public void prePersist() {
        this.fechaRegistro = LocalDateTime.now();
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
