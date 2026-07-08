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
@Table(name = "detalle_envio_mejorado")
public class DetalleEnvioMejorado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "envio_id", nullable = false)
    private Long envioId;

    @Column(name = "destinatario_nombre")
    private String destinatarioNombre;

    @Column(name = "destinatario_telefono")
    private String destinatarioTelefono;

    @Column(name = "destinatario_email")
    private String destinatarioEmail;

    @Column(name = "direccion_completa")
    private String direccionCompleta;

    @Column(name = "coordenadas_latitude")
    private Double coordenadasLatitude;

    @Column(name = "coordenadas_longitude")
    private Double coordenadasLongitude;

    @Column(name = "instrucciones_especiales")
    private String instruccionesEspeciales;

    @Column(name = "requiere_firma")
    private Boolean requiereFirma;

    @Column(name = "intentos_entrega")
    private Integer intentosEntrega;

    @Column(name = "fotografia_entrega_url")
    private String fotografiaEntregaUrl;

    @Column(name = "nombre_receptor")
    private String nombreReceptor;

    @Column(name = "fecha_entrega_real")
    private LocalDateTime fechaEntregaReal;

    @PrePersist
    protected void onCreate() {
        if (requiereFirma == null) {
            requiereFirma = false;
        }
        if (intentosEntrega == null) {
            intentosEntrega = 0;
        }
    }
}
