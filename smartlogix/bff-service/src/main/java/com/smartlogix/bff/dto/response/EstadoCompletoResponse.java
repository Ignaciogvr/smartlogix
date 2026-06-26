package com.smartlogix.bff.dto.response;

public class EstadoCompletoResponse {

    private Long pedidoId;
    private UsuarioResumen usuario;
    private String pedidoEstado;
    private String envioEstado;
    private String trackingNumber;
    private Boolean envioDisponible;

    public EstadoCompletoResponse() {
    }

    public EstadoCompletoResponse(
            Long pedidoId,
            UsuarioResumen usuario,
            String pedidoEstado,
            String envioEstado,
            String trackingNumber,
            Boolean envioDisponible
    ) {
        this.pedidoId = pedidoId;
        this.usuario = usuario;
        this.pedidoEstado = pedidoEstado;
        this.envioEstado = envioEstado;
        this.trackingNumber = trackingNumber;
        this.envioDisponible = envioDisponible;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public UsuarioResumen getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioResumen usuario) {
        this.usuario = usuario;
    }

    public String getPedidoEstado() {
        return pedidoEstado;
    }

    public void setPedidoEstado(String pedidoEstado) {
        this.pedidoEstado = pedidoEstado;
    }

    public String getEnvioEstado() {
        return envioEstado;
    }

    public void setEnvioEstado(String envioEstado) {
        this.envioEstado = envioEstado;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Boolean getEnvioDisponible() {
        return envioDisponible;
    }

    public void setEnvioDisponible(Boolean envioDisponible) {
        this.envioDisponible = envioDisponible;
    }

    public static class UsuarioResumen {
        private String id;
        private String nombre;

        public UsuarioResumen() {
        }

        public UsuarioResumen(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
    }
}
