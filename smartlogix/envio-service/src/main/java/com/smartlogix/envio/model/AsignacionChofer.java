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
@Table(name = "asignacion_chofer")
public class AsignacionChofer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "envio_id", nullable = false)
    private Long envioId;

    @Column(name = "chofer_id", nullable = false)
    private String choferId;

    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;

    @Column(name = "fecha_recogida")
    private LocalDateTime fechaRecogida;

    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;

    @Column(name = "estado_asignacion")
    private String estadoAsignacion;

    @Column(name = "calificacion_chofer_estrellas")
    private Integer calificacionChoferEstrellas;

    @Column(name = "comentario_cliente")
    private String comentarioCliente;

    @PrePersist
    protected void onCreate() {
        if (fechaAsignacion == null) {
            fechaAsignacion = LocalDateTime.now();
        }
        if (estadoAsignacion == null) {
            estadoAsignacion = "ASIGNADO";
        }
    }
}
