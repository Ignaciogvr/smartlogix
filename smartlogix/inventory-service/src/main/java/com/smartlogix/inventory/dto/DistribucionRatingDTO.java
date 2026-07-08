package com.smartlogix.inventory.dto;

/**
 * 2.14 - Distribución porcentual de ratings (estrellas) de un producto.
 * Ej: 5★ → 60%, 4★ → 20%, etc.
 */
public class DistribucionRatingDTO {
    private Integer calificacion;   // 1–5
    private Long cantidad;
    private Double porcentaje;      // calculado en servicio

    public DistribucionRatingDTO() {}
    public DistribucionRatingDTO(Integer calificacion, Long cantidad, Double porcentaje) {
        this.calificacion = calificacion;
        this.cantidad = cantidad;
        this.porcentaje = porcentaje;
    }

    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }
    public Long getCantidad() { return cantidad; }
    public void setCantidad(Long cantidad) { this.cantidad = cantidad; }
    public Double getPorcentaje() { return porcentaje; }
    public void setPorcentaje(Double porcentaje) { this.porcentaje = porcentaje; }
}
