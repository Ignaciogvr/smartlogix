package com.smartlogix.inventory.service.impl;

import com.smartlogix.inventory.model.Producto;
import com.smartlogix.inventory.model.ReservaStock;
import com.smartlogix.inventory.repository.ProductoRepository;
import com.smartlogix.inventory.repository.ReservaStockRepository;
import com.smartlogix.inventory.service.ReservaStockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaStockServiceImpl implements ReservaStockService {

    private final ReservaStockRepository reservaRepository;
    private final ProductoRepository productoRepository;

    public ReservaStockServiceImpl(ReservaStockRepository reservaRepository, ProductoRepository productoRepository) {
        this.reservaRepository = reservaRepository;
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional
    public ReservaStock crearReserva(Long productoId, Integer cantidad) {
        Producto p = productoRepository.findByIdForUpdate(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                
        if (p.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente para reserva");
        }
        
        // Descontar stock inmediatamente para evitar sobreventas
        p.descontarStock(cantidad);
        productoRepository.save(p);
        
        ReservaStock reserva = new ReservaStock();
        reserva.setProducto(p);
        reserva.setCantidad(cantidad);
        return reservaRepository.save(reserva);
    }

    @Override
    @Transactional
    public void confirmarReserva(Long reservaId) {
        ReservaStock reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        
        if (!"RESERVADA".equals(reserva.getEstado())) {
            throw new RuntimeException("Reserva no está en estado RESERVADA");
        }
        
        reserva.setEstado("CONFIRMADA");
        reservaRepository.save(reserva);
    }

    @Override
    @Transactional
    public void cancelarReserva(Long reservaId) {
        ReservaStock reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        
        if (!"RESERVADA".equals(reserva.getEstado())) {
            throw new RuntimeException("Reserva no está en estado RESERVADA");
        }
        
        // Reponer stock
        Producto p = productoRepository.findByIdForUpdate(reserva.getProducto().getId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        p.reponerStock(reserva.getCantidad());
        productoRepository.save(p);
        
        reserva.setEstado("CANCELADA");
        reservaRepository.save(reserva);
    }

    @Override
    @Transactional
    public void expirarReservasVencidas() {
        List<ReservaStock> expiradas = reservaRepository.findByEstadoAndFechaExpiracionBefore("RESERVADA", LocalDateTime.now());
        for (ReservaStock reserva : expiradas) {
            try {
                Producto p = productoRepository.findByIdForUpdate(reserva.getProducto().getId()).orElse(null);
                if (p != null) {
                    p.reponerStock(reserva.getCantidad());
                    productoRepository.save(p);
                }
                reserva.setEstado("EXPIRADA");
                reservaRepository.save(reserva);
            } catch (Exception e) {
                // Ignore failure for individual records and continue
            }
        }
    }
}
