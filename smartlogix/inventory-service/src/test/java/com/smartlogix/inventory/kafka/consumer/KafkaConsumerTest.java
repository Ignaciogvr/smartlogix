package com.smartlogix.inventory.kafka.consumer;

import com.smartlogix.inventory.event.CompraEvent;
import com.smartlogix.inventory.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private KafkaConsumer kafkaConsumer;

    @Test
    void consumir_EventoValido_DebeLlamarDescontarStock() {
        // Arrange
        CompraEvent event = new CompraEvent(1L, 5, "user123");

        // Act
        kafkaConsumer.consumir(event, "compras", 0L, null);

        // Assert
        verify(productoService, times(1)).descontarStock(
                eq(1L),
                eq(5),
                eq("user123")
        );
    }

    @Test
    void consumir_EventoNulo_DebeLanzarIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
                kafkaConsumer.consumir(null, "compras", 0L, null)
        );

        verify(productoService, never()).descontarStock(any(), any(), any());
    }

    @Test
    void consumir_ServiceLanzaSQLException_DebePropagarExcepcion() throws Exception {
        // Arrange
        CompraEvent event = new CompraEvent(1L, 5, "user123");
        doThrow(new SQLException("BD down")).when(productoService)
                .descontarStock(1L, 5, "user123");

        // Act & Assert
        assertThrows(SQLException.class, () -> 
                kafkaConsumer.consumir(event, "compras", 0L, null)
        );
    }

    @Test
    void consumir_ServiceLanzaIllegalArgumentException_DebePropagarExcepcion() {
        // Arrange
        CompraEvent event = new CompraEvent(1L, 5, "user123");
        doThrow(new IllegalArgumentException("Stock insuficiente"))
                .when(productoService).descontarStock(1L, 5, "user123");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
                kafkaConsumer.consumir(event, "compras", 0L, null)
        );
    }

    @Test
    void handleDlt_EventoValido_NoDebeLanzarExcepcion() {
        // Arrange
        CompraEvent event = new CompraEvent(1L, 5, "user123");

        // Act & Assert - no debe lanzar excepción
        kafkaConsumer.handleDlt(
                event,
                "compras-dlt",
                0L,
                "Error de prueba",
                "Stacktrace de prueba"
        );
    }

    @Test
    void handleDlt_EventoNulo_NoDebeLanzarExcepcion() {
        // Act & Assert - no debe lanzar excepción
        kafkaConsumer.handleDlt(
                null,
                "compras-dlt",
                0L,
                "Error de prueba",
                "Stacktrace de prueba"
        );
    }
}
