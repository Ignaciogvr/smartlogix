package com.smartlogix.usuarios.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios", indexes = {
    @Index(name = "idx_usuarios_auth0_id", columnList = "auth0_id"),
    @Index(name = "idx_usuarios_email", columnList = "email"),
    @Index(name = "idx_usuarios_estado", columnList = "estado")
})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true)
    private String email;

    @Column(name = "auth0_id", unique = true)
    private String auth0Id;

    private String estado = "ACTIVO";

    @Enumerated(EnumType.STRING)
    private Rol rol = Rol.CLIENTE;

    private LocalDateTime fechaSuspension;

    @Column(name = "telefono", length = 20)
    private String telefono;

    public Usuario() {}

    public Long getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAuth0Id() { return auth0Id; }
    public void setAuth0Id(String auth0Id) { this.auth0Id = auth0Id; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public LocalDateTime getFechaSuspension() { return fechaSuspension; }
    public void setFechaSuspension(LocalDateTime fechaSuspension) { this.fechaSuspension = fechaSuspension; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}