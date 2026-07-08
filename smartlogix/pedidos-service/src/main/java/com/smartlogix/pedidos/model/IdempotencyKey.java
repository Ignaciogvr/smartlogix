package com.smartlogix.pedidos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "idempotency_keys")
public class IdempotencyKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "idempotency_key", nullable = false, unique = true)
    private String key;
    
    @Column(name = "response_body", columnDefinition = "TEXT")
    private String responseBody;
    
    @Column(name = "response_status")
    private Integer responseStatus;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    @PrePersist
    public void prePersist() {
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public String getResponseBody() { return responseBody; }
    public void setResponseBody(String responseBody) { this.responseBody = responseBody; }
    public Integer getResponseStatus() { return responseStatus; }
    public void setResponseStatus(Integer responseStatus) { this.responseStatus = responseStatus; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
