package com.smartlogix.envio.kafka.consumer;

import com.smartlogix.envio.client.PedidoClient;
import com.smartlogix.envio.client.UsuarioClient;
import com.smartlogix.envio.event.PedidoCreadoEvent;
import com.smartlogix.envio.kafka.producer.KafkaProducerService;
import com.smartlogix.envio.model.Envio;
import com.smartlogix.envio.model.EstadoEnvio;
import com.smartlogix.envio.repository.EnvioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoCreadoConsumerTest {

    @Mock
    private EnvioRepository envioRepository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @Mock
    private PedidoClient pedidoClient;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private PedidoCreadoConsumer pedidoCreadoConsumer;

    @Test
    void consumirPedidoCreado_EventoValido_DebeCrearEnvio() {
        // Arrange
        PedidoCreadoEvent event = new PedidoCreadoEvent(1L, "user123", "Calle 123", 100.0, null);
        
        when(pedidoClient.obtenerPedido("1")).thenReturn(null); // Validación exitosa
        when(usuarioClient.obtenerUsuarioPorId("user123")).thenReturn(true);
        
        Envio envioGuardado = new Envio();
        envioGuardado.setPedidoId(1L);
        envioGuardado.setTrackingNumber("TRACK-12345");
        when(envioRepository.save(any(Envio.class))).thenReturn(envioGuardado);

        // Act
        pedidoCreadoConsumer.consumirPedidoCreado(event, "pedido-creado", 0L, null);

        // Assert
        verify(pedidoClient, times(1)).obtenerPedido("1");
        verify(usuarioClient, times(1)).obtenerUsuarioPorId("user123");
        verify(envioRepository, times(1)).save(any(Envio.class));
        verify(kafkaProducerService, times(1)).enviarEventoEnvio(any());
    }

    @Test
    void consumirPedidoCreado_EventoNulo_DebeLanzarIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
                pedidoCreadoConsumer.consumirPedidoCreado(null, "pedido-creado", 0L, null)
        );

        verify(pedidoClient, never()).obtenerPedido(any());
        verify(envioRepository, never()).save(any());
    }

    @Test
    void consumirPedidoCreado_UsuarioNoExiste_DebeLanzarIllegalArgumentException() {
        // Arrange
        PedidoCreadoEvent event = new PedidoCreadoEvent(1L, "user123", "Calle 123", 100.0, null);
        when(pedidoClient.obtenerPedido("1")).thenReturn(null);
        when(usuarioClient.obtenerUsuarioPorId("user123")).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> 
                pedidoCreadoConsumer.consumirPedidoCreado(event, "pedido-creado", 0L, null)
        );

        verify(envioRepository, never()).save(any());
    }

    @Test
    void consumirPedidoCreado_ErrorBD_DebePropagarSQLException() {
        // Arrange
        PedidoCreadoEvent event = new PedidoCreadoEvent(1L, "user123", "Calle 123", 100.0, null);
        when(pedidoClient.obtenerPedido("1")).thenReturn(null);
        when(usuarioClient.obtenerUsuarioPorId("user123")).thenReturn(true);
        when(envioRepository.save(any(Envio.class))).thenThrow(new RuntimeException(new SQLException("BD down")));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
                pedidoCreadoConsumer.consumirPedidoCreado(event, "pedido-creado", 0L, null)
        );
    }

    @Test
    void handleDlt_EventoValido_NoDebeLanzarExcepcion() {
        // Arrange
        PedidoCreadoEvent event = new PedidoCreadoEvent(1L, "user123", "Calle 123", 100.0, null);

        // Act & Assert
        pedidoCreadoConsumer.handleDlt(
                event,
                "pedido-creado-dlt",
                0L,
                "Error de prueba",
                "Stacktrace de prueba"
        );
    }

    @Test
    void handleDlt_EventoNulo_NoDebeLanzarExcepcion() {
        // Act & Assert
        pedidoCreadoConsumer.handleDlt(
                null,
                "pedido-creado-dlt",
                0L,
                "Error de prueba",
                "Stacktrace de prueba"
        );
    }
}
