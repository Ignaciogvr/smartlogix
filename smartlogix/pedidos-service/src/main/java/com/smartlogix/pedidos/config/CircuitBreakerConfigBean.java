package com.smartlogix.pedidos.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CircuitBreakerConfigBean {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {

        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .slidingWindowSize(10)
                .build();

        return CircuitBreakerRegistry.of(config);
    }
}