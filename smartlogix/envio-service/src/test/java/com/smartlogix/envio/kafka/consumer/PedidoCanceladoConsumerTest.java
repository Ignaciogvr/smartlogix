package com.smartlogix.envio.kafka.consumer;

import com.smartlogix.envio.event.PedidoCanceladoEvent;
import com.smartlogix.envio.service.EnvioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoCanceladoConsumerTest {

    @Mock
    private EnvioService envioService;

    @InjectMocks
    private PedidoCanceladoConsumer pedidoCanceladoConsumer;

    @Test
    void consumirPedidoCancelado_EventoValido_DebeLlamarCancelarEnvio() {
        // Arrange
        PedidoCanceladoEvent event = new PedidoCanceladoEvent(1L);

        // Act
        pedidoCanceladoConsumer.consumirPedidoCancelado(event, "pedido-cancelado", 0L, null);

        // Assert
        verify(envioService, times(1)).cancelarPorPedido(eq(1L));
    }

    @Test
    void consumirPedidoCancelado_EventoNulo_DebeLanzarIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
                pedidoCanceladoConsumer.consumirPedidoCancelado(null, "pedido-cancelado", 0L, null)
        );

        verify(envioService, never()).cancelarPorPedido(any());
    }

    @Test
    void consumirPedidoCancelado_ErrorBD_DebePropagarExcepcion() {
        // Arrange
        PedidoCanceladoEvent event = new PedidoCanceladoEvent(1L);
        doThrow(new DataAccessException("BD down") {})
                .when(envioService).cancelarPorPedido(1L);

        // Act & Assert
        assertThrows(DataAccessException.class, () -> 
                pedidoCanceladoConsumer.consumirPedidoCancelado(event, "pedido-cancelado", 0L, null)
        );
    }

    @Test
    void handleDlt_EventoValido_NoDebeLanzarExcepcion() {
        // Arrange
        PedidoCanceladoEvent event = new PedidoCanceladoEvent(1L);

        // Act & Assert
        pedidoCanceladoConsumer.handleDlt(
                event,
                "pedido-cancelado-dlt",
                0L,
                "Error de prueba",
                "Stacktrace de prueba"
        );
    }

    @Test
    void handleDlt_EventoNulo_NoDebeLanzarExcepcion() {
        // Act & Assert
        pedidoCanceladoConsumer.handleDlt(
                null,
                "pedido-cancelado-dlt",
                0L,
                "Error de prueba",
                "Stacktrace de prueba"
        );
    }
}
