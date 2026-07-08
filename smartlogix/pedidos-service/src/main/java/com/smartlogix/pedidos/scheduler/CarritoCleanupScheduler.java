package com.smartlogix.pedidos.scheduler;

import com.smartlogix.pedidos.repository.CarritoRepository;
import com.smartlogix.pedidos.model.EstadoCarrito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class CarritoCleanupScheduler {

    private static final Logger log = LoggerFactory.getLogger(CarritoCleanupScheduler.class);
    private final CarritoRepository carritoRepository;

    public CarritoCleanupScheduler(CarritoRepository carritoRepository) {
        this.carritoRepository = carritoRepository;
    }

    @Scheduled(cron = "0 0 2 * * *") // Diario a las 2 AM
    @Transactional
    public void limpiarCarritosAbandonados() {
        log.info("[CARRITO-CLEANUP] Iniciando limpieza de carritos abandonados...");
        LocalDateTime limite = LocalDateTime.now().minusDays(7);
        
        var carritos = carritoRepository.findAll();
        for (var c : carritos) {
            if (c.getEstado() == EstadoCarrito.ACTIVO && c.getFechaActualizacion().isBefore(limite)) {
                log.info("[CARRITO-CLEANUP] Marcando carrito {} como abandonado", c.getId());
                c.setEstado(EstadoCarrito.ABANDONADO);
                carritoRepository.save(c);
            }
        }
    }
}
