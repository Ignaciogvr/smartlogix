package com.smartlogix.inventory.scheduler;

import com.smartlogix.inventory.service.ReservaStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ReservaExpirationScheduler {

    private static final Logger log = LoggerFactory.getLogger(ReservaExpirationScheduler.class);
    private final ReservaStockService reservaStockService;

    public ReservaExpirationScheduler(ReservaStockService reservaStockService) {
        this.reservaStockService = reservaStockService;
    }

    @Scheduled(fixedDelay = 60000)
    public void expirarReservas() {
        log.info("[RESERVAS] Verificando reservas expiradas...");
        try {
            reservaStockService.expirarReservasVencidas();
        } catch (Exception e) {
            log.error("[RESERVAS] Error expirando reservas", e);
        }
    }
}
