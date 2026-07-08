package com.smartlogix.pedidos.service;

import com.smartlogix.pedidos.dto.CarritoItemRequest;
import com.smartlogix.pedidos.dto.CarritoResponse;

public interface CarritoService {
    CarritoResponse obtenerCarrito(String usuarioId);
    CarritoResponse agregarItem(String usuarioId, CarritoItemRequest request);
    CarritoResponse actualizarItem(String usuarioId, Long productoId, Integer cantidad);
    CarritoResponse removerItem(String usuarioId, Long productoId);
    void vaciarCarrito(String usuarioId);
}
