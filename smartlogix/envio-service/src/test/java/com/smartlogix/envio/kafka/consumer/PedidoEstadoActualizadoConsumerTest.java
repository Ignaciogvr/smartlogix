package com.smartlogix.envio.kafka.consumer;

import com.smartlogix.envio.event.PedidoEstadoActualizadoEvent;
import com.smartlogix.envio.model.Envio;
import com.smartlogix.envio.model.EstadoEnvio;
import com.smartlogix.envio.repository.EnvioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoEstadoActualizadoConsumerTest {

    @Mock
    private EnvioRepository envioRepository;

    @InjectMocks
    private PedidoEstadoActualizadoConsumer pedidoEstadoActualizadoConsumer;

    @Test
    void consumirPedidoEstadoActualizado_EventoValido_DebeActualizarEnvio() {
        // Arrange
        PedidoEstadoActualizadoEvent event = new PedidoEstadoActualizadoEvent(1L, "ENVIADO");
        
        Envio envio = new Envio();
        envio.setPedidoId(1L);
        envio.setEstado(EstadoEnvio.PREPARANDO);
        
        when(envioRepository.findByPedidoId(1L)).thenReturn(List.of(envio));
        when(envioRepository.save(any(Envio.class))).thenReturn(envio);

        // Act
        pedidoEstadoActualizadoConsumer.consumirPedidoEstadoActualizado(event, "pedido-estado-actualizado", 0L, null);

        // Assert
        verify(envioRepository, times(1)).findByPedidoId(eq(1L));
        verify(envioRepository, times(1)).save(any(Envio.class));
    }

    @Test
    void consumirPedidoEstadoActualizado_EventoNulo_DebeLanzarIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
                pedidoEstadoActualizadoConsumer.consumirPedidoEstadoActualizado(
                        null, "pedido-estado-actualizado", 0L, null
                )
        );

        verify(envioRepository, never()).findByPedidoId(any());
    }

    @Test
    void consumirPedidoEstadoActualizado_EstadoDesconocido_DebeLanzarIllegalArgumentException() {
        // Arrange
        PedidoEstadoActualizadoEvent event = new PedidoEstadoActualizadoEvent(1L, "ESTADO_DESCONOCIDO");
        
        Envio envio = new Envio();
        envio.setEstado(EstadoEnvio.PENDIENTE);
        when(envioRepository.findByPedidoId(1L)).thenReturn(List.of(envio));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
                pedidoEstadoActualizadoConsumer.consumirPedidoEstadoActualizado(
                        event, "pedido-estado-actualizado", 0L, null
                )
        );
    }

    @Test
    void consumirPedidoEstadoActualizado_NoHayEnvios_NoDebeActualizar() {
        // Arrange
        PedidoEstadoActualizadoEvent event = new PedidoEstadoActualizadoEvent(1L, "ENVIADO");
        when(envioRepository.findByPedidoId(1L)).thenReturn(Collections.emptyList());

        // Act
        pedidoEstadoActualizadoConsumer.consumirPedidoEstadoActualizado(event, "pedido-estado-actualizado", 0L, null);

        // Assert
        verify(envioRepository, times(1)).findByPedidoId(eq(1L));
        verify(envioRepository, never()).save(any());
    }

    @Test
    void consumirPedidoEstadoActualizado_ErrorBD_DebePropagarExcepcion() {
        // Arrange
        PedidoEstadoActualizadoEvent event = new PedidoEstadoActualizadoEvent(1L, "ENVIADO");
        when(envioRepository.findByPedidoId(1L)).thenThrow(new DataAccessException("BD down") {});

        // Act & Assert
        assertThrows(DataAccessException.class, () -> 
                pedidoEstadoActualizadoConsumer.consumirPedidoEstadoActualizado(
                        event, "pedido-estado-actualizado", 0L, null
                )
        );
    }

    @Test
    void handleDlt_EventoValido_NoDebeLanzarExcepcion() {
        // Arrange
        PedidoEstadoActualizadoEvent event = new PedidoEstadoActualizadoEvent(1L, "ENVIADO");

        // Act & Assert
        pedidoEstadoActualizadoConsumer.handleDlt(
                event,
                "pedido-estado-actualizado-dlt",
                0L,
                "Error de prueba",
                "Stacktrace de prueba"
        );
    }

    @Test
    void handleDlt_EventoNulo_NoDebeLanzarExcepcion() {
        // Act & Assert
        pedidoEstadoActualizadoConsumer.handleDlt(
                null,
                "pedido-estado-actualizado-dlt",
                0L,
                "Error de prueba",
                "Stacktrace de prueba"
        );
    }
}
