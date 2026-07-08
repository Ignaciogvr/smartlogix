package com.smartlogix.inventory.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "proveedores_inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_proveedor", unique = true, nullable = false, length = 255)
    private String nombreProveedor;

    @Column(name = "rut_proveedor", unique = true, length = 20)
    private String rutProveedor;

    @Column(name = "contacto_nombre", length = 255)
    private String contactoNombre;

    @Column(name = "contacto_email", length = 255)
    private String contactoEmail;

    @Column(name = "contacto_telefono", length = 20)
    private String contactoTelefono;

    @Column(length = 500)
    private String direccion;

    @Column(length = 100)
    private String ciudad;

    @Column(length = 100)
    private String pais;

    @Column(name = "tiempo_entrega_dias")
    private Integer tiempoEntregaDias;

    @Column(name = "comision_porcentaje")
    private Double comisionPorcentaje;

    @Column(name = "estado_proveedor", length = 50)
    private String estadoProveedor;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
}
