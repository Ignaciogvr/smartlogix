package com.smartlogix.pedidos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carritos")
public class Carrito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "usuario_id", nullable = false)
    private String usuarioId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCarrito estado = EstadoCarrito.ACTIVO;
    
    @Column(nullable = false)
    private Double total = 0.0;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
    
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoItem> items = new ArrayList<>();
    
    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
    
    public void recalcularTotal() {
        this.total = items.stream().mapToDouble(i -> i.getPrecio() * i.getCantidad()).sum();
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    public EstadoCarrito getEstado() { return estado; }
    public void setEstado(EstadoCarrito estado) { this.estado = estado; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public List<CarritoItem> getItems() { return items; }
    public void setItems(List<CarritoItem> items) { this.items = items; }
}
