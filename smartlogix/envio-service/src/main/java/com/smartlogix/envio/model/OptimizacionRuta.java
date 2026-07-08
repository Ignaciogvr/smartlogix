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
@Table(name = "optimizacion_rutas")
public class OptimizacionRuta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_ruta")
    private LocalDateTime fechaRuta;

    @Column(name = "zona_cobertura")
    private String zonaCobertura;

    @Column(name = "cantidad_envios")
    private Integer cantidadEnvios;

    @Column(name = "distancia_total_km")
    private Double distanciaTotalKm;

    @Column(name = "tiempo_total_minutos")
    private Integer tiempoTotalMinutos;

    @Column(name = "efficiency_score")
    private Double efficiencyScore;

    @Column(name = "costo_estimado")
    private Double costoEstimado;

    @Column(name = "observaciones")
    private String observaciones;
}
