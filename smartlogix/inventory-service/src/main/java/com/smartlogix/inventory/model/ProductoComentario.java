package com.smartlogix.inventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "producto_comentarios")
public class ProductoComentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "usuario_id")
    private String usuarioId;

    @Column(name = "pedido_id")
    private Long pedidoId;

    @Column(name = "nombre_cliente", nullable = false)
    private String nombreCliente;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(nullable = false)
    private Integer calificacion;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comentario;

    @Column(name = "compra_verificada", nullable = false)
    private Boolean compraVerificada = false;

    @Column(name = "respuesta_empresa", columnDefinition = "TEXT")
    private String respuestaEmpresa;

    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    @Column(nullable = false)
    private Boolean destacado = false;

    @Column(name = "votos_utilidad", nullable = false)
    private Integer votosUtilidad = 0;

    @Column(nullable = false)
    private Boolean recomendado = false;


    @Column(nullable = false)
    private Integer reportes = 0;

    public ProductoComentario() {}

    public Long getId() { return id; }
    public String getUsuarioId() { return usuarioId; }
    public String getNombreCliente() { return nombreCliente; }
    public LocalDateTime getFecha() { return fecha; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public Boolean getActivo() { return activo; }
    public Integer getCalificacion() { return calificacion; }
    public String getComentario() { return comentario; }
    public Boolean getCompraVerificada() { return compraVerificada; }
    public String getRespuestaEmpresa() { return respuestaEmpresa; }
    public String getImagenUrl() { return imagenUrl; }
    public Boolean getDestacado() { return destacado; }
    public Integer getVotosUtilidad() { return votosUtilidad; }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setId(Long id) { this.id = id; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public void setCalificacion(Integer calificacion) { this.calificacion = calificacion; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public void setCompraVerificada(Boolean compraVerificada) { this.compraVerificada = compraVerificada; }
    public void setRespuestaEmpresa(String respuestaEmpresa) { this.respuestaEmpresa = respuestaEmpresa; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public void setDestacado(Boolean destacado) { this.destacado = destacado; }
    public void setVotosUtilidad(Integer votosUtilidad) { this.votosUtilidad = votosUtilidad; }

    public Boolean getRecomendado() { return recomendado; }
    public void setRecomendado(Boolean recomendado) { this.recomendado = recomendado; }


    public Integer getReportes() { return reportes; }
    public void setReportes(Integer reportes) { this.reportes = reportes; }
}
