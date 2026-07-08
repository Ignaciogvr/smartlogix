package com.smartlogix.usuarios.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria_usuarios")
public class AuditoriaUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private String accion;

    @Column(name = "tabla_afectada")
    private String tablaAfectada;

    @Column(name = "valores_anteriores")
    private String valoresAnteriores;

    @Column(name = "valores_nuevos")
    private String valoresNuevos;

    @Column(name = "usuario_realizador")
    private String usuarioRealizador;

    @Column(name = "fecha_accion", updatable = false)
    private LocalDateTime fechaAccion;

    @Column(name = "ip_origin")
    private String ipOrigin;

    @PrePersist
    protected void onCreate() {
        fechaAccion = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
    public String getTablaAfectada() { return tablaAfectada; }
    public void setTablaAfectada(String tablaAfectada) { this.tablaAfectada = tablaAfectada; }
    public String getValoresAnteriores() { return valoresAnteriores; }
    public void setValoresAnteriores(String valoresAnteriores) { this.valoresAnteriores = valoresAnteriores; }
    public String getValoresNuevos() { return valoresNuevos; }
    public void setValoresNuevos(String valoresNuevos) { this.valoresNuevos = valoresNuevos; }
    public String getUsuarioRealizador() { return usuarioRealizador; }
    public void setUsuarioRealizador(String usuarioRealizador) { this.usuarioRealizador = usuarioRealizador; }
    public LocalDateTime getFechaAccion() { return fechaAccion; }
    public void setFechaAccion(LocalDateTime fechaAccion) { this.fechaAccion = fechaAccion; }
    public String getIpOrigin() { return ipOrigin; }
    public void setIpOrigin(String ipOrigin) { this.ipOrigin = ipOrigin; }
}
