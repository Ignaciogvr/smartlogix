package com.smartlogix.inventory.dto;

public class ComentarioModeracionResponseDTO {
    private Long id;
    private Long comentarioId;
    private Long moderadorId;
    private String accion;
    private String fechaAccion;

    public ComentarioModeracionResponseDTO() {}

    public ComentarioModeracionResponseDTO(Long id, Long comentarioId, Long moderadorId, String accion, String fechaAccion) {
        this.id = id;
        this.comentarioId = comentarioId;
        this.moderadorId = moderadorId;
        this.accion = accion;
        this.fechaAccion = fechaAccion;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getComentarioId() { return comentarioId; }
    public void setComentarioId(Long comentarioId) { this.comentarioId = comentarioId; }
    public Long getModeradorId() { return moderadorId; }
    public void setModeradorId(Long moderadorId) { this.moderadorId = moderadorId; }
    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
    public String getFechaAccion() { return fechaAccion; }
    public void setFechaAccion(String fechaAccion) { this.fechaAccion = fechaAccion; }
}
