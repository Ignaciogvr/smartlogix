package com.smartlogix.bff.model;

import java.util.List;

public class CatalogoView {

    // =========================
    // INFO CATALOGO
    // =========================

    private String categoria;

    private Integer totalProductos;

    private Integer productosDisponibles;

    private Integer productosSinStock;

    // =========================
    // PRODUCTOS
    // =========================

    private List<Long> productosDestacados;

    private List<Long> productosNuevos;

    private List<Long> productosOferta;

    // =========================
    // FILTROS
    // =========================

    private Double precioMinimo;

    private Double precioMaximo;

    private List<String> marcas;

    // =========================
    // CONSTRUCTORES
    // =========================

    public CatalogoView() {
    }

    public CatalogoView(
            String categoria,
            Integer totalProductos,
            Integer productosDisponibles,
            Integer productosSinStock,
            List<Long> productosDestacados,
            List<Long> productosNuevos,
            List<Long> productosOferta,
            Double precioMinimo,
            Double precioMaximo,
            List<String> marcas
    ) {
        this.categoria = categoria;
        this.totalProductos = totalProductos;
        this.productosDisponibles = productosDisponibles;
        this.productosSinStock = productosSinStock;
        this.productosDestacados = productosDestacados;
        this.productosNuevos = productosNuevos;
        this.productosOferta = productosOferta;
        this.precioMinimo = precioMinimo;
        this.precioMaximo = precioMaximo;
        this.marcas = marcas;
    }

    // =========================
    // GETTERS & SETTERS
    // =========================

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(Integer totalProductos) {
        this.totalProductos = totalProductos;
    }

    public Integer getProductosDisponibles() {
        return productosDisponibles;
    }

    public void setProductosDisponibles(Integer productosDisponibles) {
        this.productosDisponibles = productosDisponibles;
    }

    public Integer getProductosSinStock() {
        return productosSinStock;
    }

    public void setProductosSinStock(Integer productosSinStock) {
        this.productosSinStock = productosSinStock;
    }

    public List<Long> getProductosDestacados() {
        return productosDestacados;
    }

    public void setProductosDestacados(List<Long> productosDestacados) {
        this.productosDestacados = productosDestacados;
    }

    public List<Long> getProductosNuevos() {
        return productosNuevos;
    }

    public void setProductosNuevos(List<Long> productosNuevos) {
        this.productosNuevos = productosNuevos;
    }

    public List<Long> getProductosOferta() {
        return productosOferta;
    }

    public void setProductosOferta(List<Long> productosOferta) {
        this.productosOferta = productosOferta;
    }

    public Double getPrecioMinimo() {
        return precioMinimo;
    }

    public void setPrecioMinimo(Double precioMinimo) {
        this.precioMinimo = precioMinimo;
    }

    public Double getPrecioMaximo() {
        return precioMaximo;
    }

    public void setPrecioMaximo(Double precioMaximo) {
        this.precioMaximo = precioMaximo;
    }

    public List<String> getMarcas() {
        return marcas;
    }

    public void setMarcas(List<String> marcas) {
        this.marcas = marcas;
    }
}