package com.smartlogix.bff.dto;

import java.util.List;

public class CatalogoResponse {

    private List<ProductoResponse> productos;

    public CatalogoResponse() {}

    public CatalogoResponse(List<ProductoResponse> productos) {
        this.productos = productos;
    }

    public List<ProductoResponse> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoResponse> productos) {
        this.productos = productos;
    }
}