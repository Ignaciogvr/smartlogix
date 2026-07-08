package com.smartlogix.inventory.service;

import com.smartlogix.inventory.model.ReservaStock;

public interface ReservaStockService {
    ReservaStock crearReserva(Long productoId, Integer cantidad);
    void confirmarReserva(Long reservaId);
    void cancelarReserva(Long reservaId);
    void expirarReservasVencidas();
}
