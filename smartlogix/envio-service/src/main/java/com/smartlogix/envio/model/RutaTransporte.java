package com.smartlogix.envio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rutas_transporte")
public class RutaTransporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transportista_id", nullable = false)
    private Transportista transportista;

    @Column(name = "zona_origen")
    private String zonaOrigen;

    @Column(name = "zona_destino")
    private String zonaDestino;

    @Column(name = "tiempo_promedio_dias")
    private Integer tiempoPromedioDias;

    @Column(name = "costo_base")
    private Double costoBase;

    @Column(name = "costo_por_kg")
    private Double costoPorKg;

    @Column(name = "activa")
    private Boolean activa;

    @PrePersist
    protected void onCreate() {
        if (activa == null) {
            activa = true;
        }
        if (costoPorKg == null) {
            costoPorKg = 0.0;
        }
    }
}
