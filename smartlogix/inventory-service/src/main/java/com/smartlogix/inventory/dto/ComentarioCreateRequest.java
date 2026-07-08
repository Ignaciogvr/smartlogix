package com.smartlogix.inventory.dto;

public class ComentarioCreateRequest {
    private Integer calificacion;
    private String comentario;
    private Long pedidoId;
    private String imagenUrl;
    private Boolean recomendado;

    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }

    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public Boolean getRecomendado() { return recomendado; }
    public void setRecomendado(Boolean recomendado) { this.recomendado = recomendado; }
}
