package com.smartlogix.pedidos.dto;

import java.time.LocalDateTime;

public class PagoDTO {

    private Long id;
    private Double monto;
    private String estado;
    private LocalDateTime fechaTransaccion;
    private LocalDateTime fechaConfirmacion;
    private String estadoPago;
    private String metodoPago;
    private String numeroReferencia;

    public PagoDTO() {}

    public PagoDTO(Long id, Double monto, String estado, LocalDateTime fechaTransaccion,
                   LocalDateTime fechaConfirmacion, String estadoPago, String metodoPago, 
                   String numeroReferencia) {
        this.id = id;
        this.monto = monto;
        this.estado = estado;
        this.fechaTransaccion = fechaTransaccion;
        this.fechaConfirmacion = fechaConfirmacion;
        this.estadoPago = estadoPago;
        this.metodoPago = metodoPago;
        this.numeroReferencia = numeroReferencia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(LocalDateTime fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public LocalDateTime getFechaConfirmacion() {
        return fechaConfirmacion;
    }

    public void setFechaConfirmacion(LocalDateTime fechaConfirmacion) {
        this.fechaConfirmacion = fechaConfirmacion;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getNumeroReferencia() {
        return numeroReferencia;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }
}
