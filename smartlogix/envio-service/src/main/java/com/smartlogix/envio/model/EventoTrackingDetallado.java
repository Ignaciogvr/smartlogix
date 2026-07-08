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
@Table(name = "eventos_tracking_detallados")
public class EventoTrackingDetallado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "envio_id", nullable = false)
    private Long envioId;

    @Column(name = "tipo_evento", nullable = false)
    private String tipoEvento;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "ubicacion")
    private String ubicacion;

    @Column(name = "latitud")
    private Double latitud;

    @Column(name = "longitud")
    private Double longitud;

    @Column(name = "timestamp_evento")
    private LocalDateTime timestampEvento;

    @Column(name = "registrado_por")
    private String registradoPor;

    @Column(name = "foto_evento_url")
    private String fotoEventoUrl;

    @PrePersist
    protected void onCreate() {
        if (timestampEvento == null) {
            timestampEvento = LocalDateTime.now();
        }
    }
}
