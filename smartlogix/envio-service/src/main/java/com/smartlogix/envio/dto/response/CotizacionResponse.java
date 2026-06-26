package com.smartlogix.envio.dto.response;

import java.math.BigDecimal;

public class CotizacionResponse {

    private BigDecimal costo;
    private int tiempoEstimadoDias;
    private String transportista;

    public CotizacionResponse() {}

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public int getTiempoEstimadoDias() {
        return tiempoEstimadoDias;
    }

    public void setTiempoEstimadoDias(int tiempoEstimadoDias) {
        this.tiempoEstimadoDias = tiempoEstimadoDias;
    }

    public String getTransportista() {
        return transportista;
    }

    public void setTransportista(String transportista) {
        this.transportista = transportista;
    }
}