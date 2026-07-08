package com.smartlogix.bff.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO para crear un producto desde el panel de administración.
 * Se hace forward al inventory-service: POST /admin/productos
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductoCreateRequest {

    private String nombre;

    private String descripcion;

    private String descripcionCorta;

    private Double precio;

    private Double precioAnterior;

    private Integer stock;

    private String categoria;

    private String marca;

    private String modelo;

    private String fabricante;

    private String sku;

    private String imagenPrincipal;

    private Boolean activo;

    private Boolean destacado;

    private Boolean oferta;

    private Boolean nuevo;

    private Integer descuentoPorcentaje;

    private String garantia;

    private String peso;

    private String dimensiones;

    private String material;

    private String color;

    private String paisFabricacion;

    private String vendedorId;

    public ProductoCreateRequest() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDescripcionCorta() { return descripcionCorta; }
    public void setDescripcionCorta(String descripcionCorta) { this.descripcionCorta = descripcionCorta; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Double getPrecioAnterior() { return precioAnterior; }
    public void setPrecioAnterior(Double precioAnterior) { this.precioAnterior = precioAnterior; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getFabricante() { return fabricante; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getImagenPrincipal() { return imagenPrincipal; }
    public void setImagenPrincipal(String imagenPrincipal) { this.imagenPrincipal = imagenPrincipal; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public Boolean getDestacado() { return destacado; }
    public void setDestacado(Boolean destacado) { this.destacado = destacado; }

    public Boolean getOferta() { return oferta; }
    public void setOferta(Boolean oferta) { this.oferta = oferta; }

    public Boolean getNuevo() { return nuevo; }
    public void setNuevo(Boolean nuevo) { this.nuevo = nuevo; }

    public Integer getDescuentoPorcentaje() { return descuentoPorcentaje; }
    public void setDescuentoPorcentaje(Integer descuentoPorcentaje) { this.descuentoPorcentaje = descuentoPorcentaje; }

    public String getGarantia() { return garantia; }
    public void setGarantia(String garantia) { this.garantia = garantia; }

    public String getPeso() { return peso; }
    public void setPeso(String peso) { this.peso = peso; }

    public String getDimensiones() { return dimensiones; }
    public void setDimensiones(String dimensiones) { this.dimensiones = dimensiones; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getPaisFabricacion() { return paisFabricacion; }
    public void setPaisFabricacion(String paisFabricacion) { this.paisFabricacion = paisFabricacion; }

    public String getVendedorId() { return vendedorId; }
    public void setVendedorId(String vendedorId) { this.vendedorId = vendedorId; }
}
