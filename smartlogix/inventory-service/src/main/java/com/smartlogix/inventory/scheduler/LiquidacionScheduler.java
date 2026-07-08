package com.smartlogix.inventory.scheduler;

import com.smartlogix.inventory.model.Producto;
import com.smartlogix.inventory.repository.ProductoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class LiquidacionScheduler {
    
    private static final Logger log = LoggerFactory.getLogger(LiquidacionScheduler.class);
    private final ProductoRepository productoRepository;

    public LiquidacionScheduler(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Scheduled(cron = "0 0 2 * * *") // Todos los días a las 2 AM
    @Transactional
    public void aplicarLiquidacion() {
        log.info("[LIQUIDACION] Buscando productos para liquidación...");
        LocalDateTime limite = LocalDateTime.now().minusDays(90);
        
        List<Producto> productos = productoRepository.findAll();
        for (Producto p : productos) {
            if ("ACTIVO".equals(p.getEstado().name()) && p.getStock() > 50) {
                boolean debeLiquidar = p.getFechaUltimaVenta() == null 
                    ? p.getFechaCreacion().isBefore(limite) // si no tiene ventas y es viejo
                    : p.getFechaUltimaVenta().isBefore(limite);

                if (debeLiquidar && (p.getDescuentoPorcentaje() == null || p.getDescuentoPorcentaje() < 15)) {
                    log.info("[LIQUIDACION] Aplicando descuento a producto id={}", p.getId());
                    if (p.getPrecioAnterior() == null) {
                        p.setPrecioAnterior(p.getPrecio());
                    }
                    p.setDescuentoPorcentaje(15);
                    p.setPrecio(p.getPrecioAnterior() * 0.85);
                    p.setOferta(true);
                    productoRepository.save(p);
                }
            }
        }
    }
}
