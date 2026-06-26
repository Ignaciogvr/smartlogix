package com.smartlogix.pedidos.kafka.producer;

import com.smartlogix.pedidos.event.CompraEvent;
import com.smartlogix.pedidos.event.PedidoCanceladoEvent;
import com.smartlogix.pedidos.event.PedidoCreadoEvent;
import com.smartlogix.pedidos.event.PedidoEstadoActualizadoEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaProducerServiceTest {

    @Mock
    private KafkaTemplate<String, CompraEvent> kafkaTemplate;

    @Mock
    private KafkaTemplate<String, PedidoCreadoEvent> pedidoCreadoKafkaTemplate;

    @Mock
    private KafkaTemplate<String, PedidoCanceladoEvent> pedidoCanceladoKafkaTemplate;

    @Mock
    private KafkaTemplate<String, PedidoEstadoActualizadoEvent> pedidoEstadoActualizadoKafkaTemplate;

    @InjectMocks
    private KafkaProducerService kafkaProducerService;

    @Test
    void enviarEventoCompra_DebeEnviarEventoCorrectamente() {
        // Arrange
        CompletableFuture<SendResult<String, CompraEvent>> future = CompletableFuture.completedFuture(null);
        when(kafkaTemplate.send(eq("compras"), any(CompraEvent.class))).thenReturn(future);

        // Act
        kafkaProducerService.enviarEventoCompra(1L, 5, "user123");

        // Assert
        verify(kafkaTemplate, times(1)).send(eq("compras"), any(CompraEvent.class));
    }

    @Test
    void enviarEventoPedidoCreado_DebeEnviarEventoCorrectamente() {
        // Arrange
        PedidoCreadoEvent event = new PedidoCreadoEvent(1L, "user123", "Calle 123", 100.0, null);
        CompletableFuture<SendResult<String, PedidoCreadoEvent>> future = CompletableFuture.completedFuture(null);
        when(pedidoCreadoKafkaTemplate.send(eq("pedido-creado"), eq(event))).thenReturn(future);

        // Act
        kafkaProducerService.enviarEventoPedidoCreado(event);

        // Assert
        verify(pedidoCreadoKafkaTemplate, times(1)).send(eq("pedido-creado"), eq(event));
    }

    @Test
    void enviarEventoPedidoCancelado_DebeEnviarEventoCorrectamente() {
        // Arrange
        CompletableFuture<SendResult<String, PedidoCanceladoEvent>> future = CompletableFuture.completedFuture(null);
        when(pedidoCanceladoKafkaTemplate.send(eq("pedido-cancelado"), any(PedidoCanceladoEvent.class)))
                .thenReturn(future);

        // Act
        kafkaProducerService.enviarEventoPedidoCancelado(1L);

        // Assert
        verify(pedidoCanceladoKafkaTemplate, times(1))
                .send(eq("pedido-cancelado"), any(PedidoCanceladoEvent.class));
    }

    @Test
    void enviarEventoPedidoEstadoActualizado_DebeEnviarEventoCorrectamente() {
        // Arrange
        CompletableFuture<SendResult<String, PedidoEstadoActualizadoEvent>> future = CompletableFuture.completedFuture(null);
        when(pedidoEstadoActualizadoKafkaTemplate.send(eq("pedido-estado-actualizado"), any(PedidoEstadoActualizadoEvent.class)))
                .thenReturn(future);

        // Act
        kafkaProducerService.enviarEventoPedidoEstadoActualizado(1L, "ENVIADO");

        // Assert
        verify(pedidoEstadoActualizadoKafkaTemplate, times(1))
                .send(eq("pedido-estado-actualizado"), any(PedidoEstadoActualizadoEvent.class));
    }

    @Test
    void enviarEventoCompra_ErrorEnEnvio_DebeLoguearError() {
        // Arrange
        CompletableFuture<SendResult<String, CompraEvent>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Error de Kafka"));
        when(kafkaTemplate.send(eq("compras"), any(CompraEvent.class))).thenReturn(future);

        // Act
        kafkaProducerService.enviarEventoCompra(1L, 5, "user123");

        // Assert
        verify(kafkaTemplate, times(1)).send(eq("compras"), any(CompraEvent.class));
        // El error se maneja en el callback whenComplete
    }
}
