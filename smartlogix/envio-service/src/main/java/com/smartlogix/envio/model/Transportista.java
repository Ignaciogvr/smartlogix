package com.smartlogix.envio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transportistas")
public class Transportista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_transportista", nullable = false, unique = true)
    private String nombreTransportista;

    @Column(name = "rut_transportista", unique = true)
    private String rutTransportista;

    @Column(name = "codigo_integracion")
    private String codigoIntegracion;

    @Column(name = "email_contacto")
    private String emailContacto;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "sitio_web")
    private String sitioWeb;

    @Column(name = "api_endpoint")
    private String apiEndpoint;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "estado_transportista")
    private String estadoTransportista;

    @Column(name = "comision_porcentaje")
    private Double comisionPorcentaje;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @OneToMany(mappedBy = "transportista", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RutaTransporte> rutas;

    @PrePersist
    protected void onCreate() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
        if (estadoTransportista == null) {
            estadoTransportista = "ACTIVO";
        }
        if (comisionPorcentaje == null) {
            comisionPorcentaje = 0.0;
        }
    }
}
