package com.smartlogix.inventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_reservas_stock")
public class ReservaStock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    
    @Column(nullable = false)
    private Integer cantidad;
    
    @Column(nullable = false)
    private String estado = "RESERVADA"; // RESERVADA, CONFIRMADA, EXPIRADA, CANCELADA
    
    @Column(name = "fecha_reserva", nullable = false)
    private LocalDateTime fechaReserva;
    
    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;
    
    @PrePersist
    public void prePersist() {
        this.fechaReserva = LocalDateTime.now();
        if (this.fechaExpiracion == null) {
            this.fechaExpiracion = this.fechaReserva.plusMinutes(10); // 10 min TTL
        }
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDateTime fechaReserva) { this.fechaReserva = fechaReserva; }
    public LocalDateTime getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(LocalDateTime fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }
}
