package com.smartlogix.inventory.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InventoryHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {

        boolean serviceOk = true; // puedes hacer checks reales después

        if (serviceOk) {
            return Health.up()
                    .withDetail("service", "inventory-service")
                    .withDetail("status", "running")
                    .withDetail("time", LocalDateTime.now().toString())
                    .build();
        }

        return Health.down()
                .withDetail("service", "inventory-service")
                .withDetail("error", "service unavailable")
                .build();
    }
}