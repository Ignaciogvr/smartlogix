package com.smartlogix.pedidos.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PedidoHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {

        // aquí podrías meter validaciones reales si quieres subir nivel
        boolean serviceOk = true;

        if (serviceOk) {
            return Health.up()
                    .withDetail("service", "pedidos-service")
                    .withDetail("status", "OK")
                    .withDetail("timestamp", LocalDateTime.now())
                    .build();
        }

        return Health.down()
                .withDetail("service", "pedidos-service")
                .withDetail("status", "FAIL")
                .build();
    }
}