package com.smartlogix.inventory.kafka.producer;

import com.smartlogix.inventory.event.ProductoCreadoEvent;
import com.smartlogix.inventory.event.StockDescontadoEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaProducerTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private KafkaProducer kafkaProducer;

    @Test
    void enviarProductoCreado_EventoValido_DebeLlamarSend() {
        // Arrange
        ProductoCreadoEvent event = new ProductoCreadoEvent(1L, "Laptop", "Electronics", 1500.0, 10, null);

        // Act
        kafkaProducer.enviarProductoCreado(event);

        // Assert
        verify(kafkaTemplate, times(1)).send(
                eq("producto-creado"),
                eq("1"),
                eq(event)
        );
    }

    @Test
    void enviarProductoCreado_EventoNulo_NoDebeLlamarSend() {
        // Act
        kafkaProducer.enviarProductoCreado(null);

        // Assert
        verify(kafkaTemplate, never()).send(any(), any(), any());
    }

    @Test
    void enviarProductoCreado_EventoSinId_NoDebeLlamarSend() {
        // Arrange
        ProductoCreadoEvent event = new ProductoCreadoEvent(null, "Laptop", "Electronics", 1500.0, 10, null);

        // Act
        kafkaProducer.enviarProductoCreado(event);

        // Assert
        verify(kafkaTemplate, never()).send(any(), any(), any());
    }

    @Test
    void enviarStockDescontado_EventoValido_DebeLlamarSend() {
        // Arrange
        StockDescontadoEvent event = new StockDescontadoEvent(1L, 5, "user123");

        // Act
        kafkaProducer.enviarStockDescontado(event);

        // Assert
        verify(kafkaTemplate, times(1)).send(
                eq("stock-descontado"),
                eq("1"),
                eq(event)
        );
    }

    @Test
    void enviarStockDescontado_EventoNulo_NoDebeLlamarSend() {
        // Act
        kafkaProducer.enviarStockDescontado(null);

        // Assert
        verify(kafkaTemplate, never()).send(any(), any(), any());
    }

    @Test
    void enviarStockDescontado_EventoSinProductoId_NoDebeLlamarSend() {
        // Arrange
        StockDescontadoEvent event = new StockDescontadoEvent(null, 5, "user123");

        // Act
        kafkaProducer.enviarStockDescontado(event);

        // Assert
        verify(kafkaTemplate, never()).send(any(), any(), any());
    }
}
