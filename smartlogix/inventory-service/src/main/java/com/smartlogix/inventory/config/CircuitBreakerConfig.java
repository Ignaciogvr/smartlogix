package com.smartlogix.inventory.config;

import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CircuitBreakerConfig {

    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    public CircuitBreakerConfig(CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Bean
    public CircuitBreaker inventoryCircuitBreaker() {
        return circuitBreakerFactory.create("inventory-service");
    }
}