package com.smartlogix.bff.dto.response;

import java.util.List;

public class HomeResponse {

    private List<ProductoCatalogoDTO> destacados;

    private List<ProductoCatalogoDTO> ofertas;

    private List<ProductoCatalogoDTO> recientes;

    private Integer totalProductos;

    private Integer totalCategorias;

    public HomeResponse() {
    }

    public HomeResponse(
            List<ProductoCatalogoDTO> destacados,
            List<ProductoCatalogoDTO> ofertas,
            List<ProductoCatalogoDTO> recientes,
            Integer totalProductos,
            Integer totalCategorias
    ) {
        this.destacados = destacados;
        this.ofertas = ofertas;
        this.recientes = recientes;
        this.totalProductos = totalProductos;
        this.totalCategorias = totalCategorias;
    }

    public List<ProductoCatalogoDTO> getDestacados() {
        return destacados;
    }

    public void setDestacados(List<ProductoCatalogoDTO> destacados) {
        this.destacados = destacados;
    }

    public List<ProductoCatalogoDTO> getOfertas() {
        return ofertas;
    }

    public void setOfertas(List<ProductoCatalogoDTO> ofertas) {
        this.ofertas = ofertas;
    }

    public List<ProductoCatalogoDTO> getRecientes() {
        return recientes;
    }

    public void setRecientes(List<ProductoCatalogoDTO> recientes) {
        this.recientes = recientes;
    }

    public Integer getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(Integer totalProductos) {
        this.totalProductos = totalProductos;
    }

    public Integer getTotalCategorias() {
        return totalCategorias;
    }

    public void setTotalCategorias(Integer totalCategorias) {
        this.totalCategorias = totalCategorias;
    }
}