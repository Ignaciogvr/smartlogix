package com.smartlogix.envio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "intentos_entrega")
public class IntentoEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "envio_id", nullable = false)
    private Long envioId;

    @Column(name = "numero_intento")
    private Integer numeroIntento;

    @Column(name = "fecha_intento")
    private LocalDateTime fechaIntento;

    @Column(name = "motivo_fallo")
    private String motivoFallo;

    @Column(name = "foto_domicilio_url")
    private String fotoDomicilioUrl;

    @Column(name = "proxima_fecha_reintento")
    private LocalDateTime proximaFechaReintento;

    @Column(name = "observaciones")
    private String observaciones;

    @PrePersist
    protected void onCreate() {
        if (fechaIntento == null) {
            fechaIntento = LocalDateTime.now();
        }
    }
}
