package com.smartlogix.pedidos.service;

import com.smartlogix.pedidos.model.Devolucion;
import com.smartlogix.pedidos.model.EstadoDevolucion;

import java.util.List;
import java.util.Optional;

public interface DevolucionService {
    Devolucion crearDevolucion(Devolucion devolucion);
    Optional<Devolucion> obtenerDevolucionPorId(Long id);
    List<Devolucion> obtenerDevolucionesPorPedido(Long pedidoId);
    List<Devolucion> obtenerDevolucionesPorUsuario(String usuarioId);
    List<Devolucion> obtenerTodasLasDevoluciones();
    Devolucion actualizarEstadoDevolucion(Long id, EstadoDevolucion nuevoEstado, String observaciones);
    void eliminarDevolucion(Long id);
}
