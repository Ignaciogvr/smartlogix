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

    private Long envioId;

    @Enumerated(EnumType.STRING)
    private EstadoEnvio estado;

    private String descripcion;

    private LocalDateTime fecha;

    public HistorialEnvio() {}

    public HistorialEnvio(Long envioId, EstadoEnvio estado, String descripcion) {
        this.envioId = envioId;
        this.estado = estado;
        this.descripcion = descripcion;
        this.fecha = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getEnvioId() {
        return envioId;
    }

    public EstadoEnvio getEstado() {
        return estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}