package com.smartlogix.inventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_navegacion")
public class HistorialNavegacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private String usuarioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "fecha_vista", nullable = false)
    private LocalDateTime fechaVista;

    @PrePersist
    protected void onCreate() {
        if (this.fechaVista == null) {
            this.fechaVista = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
    public LocalDateTime getFechaVista() { return fechaVista; }
    public void setFechaVista(LocalDateTime fechaVista) { this.fechaVista = fechaVista; }
}
