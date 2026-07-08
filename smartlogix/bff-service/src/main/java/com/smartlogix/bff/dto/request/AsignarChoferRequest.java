package com.smartlogix.bff.dto.request;

/**
 * DTO para asignar un chofer a un envío pendiente.
 * Usado por ADMIN para asignar manualmente un chofer de la flota interna.
 */
public class AsignarChoferRequest {
    
    private String choferId;      // ID del usuario con rol CHOFER (ej: auth0|chofer1)
    private String choferNombre;  // Nombre del chofer (opcional, para caché)
    
    public AsignarChoferRequest() {}
    
    public AsignarChoferRequest(String choferId, String choferNombre) {
        this.choferId = choferId;
        this.choferNombre = choferNombre;
    }
    
    public String getChoferId() { 
        return choferId; 
    }
    
    public void setChoferId(String choferId) { 
        this.choferId = choferId; 
    }
    
    public String getChoferNombre() { 
        return choferNombre; 
    }
    
    public void setChoferNombre(String choferNombre) { 
        this.choferNombre = choferNombre; 
    }
}
