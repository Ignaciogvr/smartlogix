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
@Table(name = "reclamos_logistica")
public class ReclamoLogistica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "envio_id", nullable = false)
    private Long envioId;

    @Column(name = "tipo_reclamo", nullable = false)
    private String tipoReclamo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "foto_evidencia_url")
    private String fotoEvidenciaUrl;

    @Column(name = "estado_reclamo")
    private String estadoReclamo;

    @Column(name = "fecha_reclamo")
    private LocalDateTime fechaReclamo;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    @Column(name = "monto_compensacion")
    private Double montoCompensacion;

    @Column(name = "resuelto_por")
    private String resueltoPor;

    @PrePersist
    protected void onCreate() {
        if (fechaReclamo == null) {
            fechaReclamo = LocalDateTime.now();
        }
        if (estadoReclamo == null) {
            estadoReclamo = "REGISTRADO";
        }
    }
}
