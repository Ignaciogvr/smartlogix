package com.smartlogix.envio.dto.request;

public class EntregarRequest {
    private String nombreReceptor;
    private String firmaReceptor; // Base64 or URL
    private String observaciones;
    private String fotoEntregaUrl;
    private String firmaReceptorUrl;

    public EntregarRequest() {}

    public String getNombreReceptor() { return nombreReceptor; }
    public void setNombreReceptor(String nombreReceptor) { this.nombreReceptor = nombreReceptor; }

    public String getFirmaReceptor() { return firmaReceptor; }
    public void setFirmaReceptor(String firmaReceptor) { this.firmaReceptor = firmaReceptor; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getFotoEntregaUrl() { return fotoEntregaUrl; }
    public void setFotoEntregaUrl(String fotoEntregaUrl) { this.fotoEntregaUrl = fotoEntregaUrl; }

    public String getFirmaReceptorUrl() { return firmaReceptorUrl; }
    public void setFirmaReceptorUrl(String firmaReceptorUrl) { this.firmaReceptorUrl = firmaReceptorUrl; }
}
