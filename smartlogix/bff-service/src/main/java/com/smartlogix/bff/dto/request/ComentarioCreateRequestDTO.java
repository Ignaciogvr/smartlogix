package com.smartlogix.bff.dto.request;

public class ComentarioCreateRequestDTO {
    private Integer calificacion;
    private String comentario;

    public Integer getCalificacion() { return calificacion; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}
