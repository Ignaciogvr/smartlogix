package com.smartlogix.envio.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_envios", indexes = {
    @Index(name = "idx_historial_envios_envio_id", columnList = "envio_id"),
    @Index(name = "idx_historial_envios_fecha", columnList = "fecha DESC")
})
public class HistorialEnvio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "envio_id", nullable = false)
    private Envio envio;

    @Enumerated(EnumType.STRING)
    private EstadoEnvio estado;

    private String descripcion;

    private LocalDateTime fecha;

    public HistorialEnvio() {}

    public HistorialEnvio(Envio envio, EstadoEnvio estado, String descripcion) {
        this.envio = envio;
        this.estado = estado;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Envio getEnvio() {
        return envio;
    }

    public void setEnvio(Envio envio) {
        this.envio = envio;
    }

    public EstadoEnvio getEstado() {
        return estado;
    }

    public void setEstado(EstadoEnvio estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}