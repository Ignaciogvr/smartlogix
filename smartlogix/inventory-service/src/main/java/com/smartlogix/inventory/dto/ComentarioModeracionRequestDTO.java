package com.smartlogix.inventory.dto;

public class ComentarioModeracionRequestDTO {
    private String accion; // OCULTADO, RESTAURADO, ELIMINADO, BLOQUEADO
    private Long moderadorId;

    public ComentarioModeracionRequestDTO() {}

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public Long getModeradorId() { return moderadorId; }
    public void setModeradorId(Long moderadorId) { this.moderadorId = moderadorId; }
}
