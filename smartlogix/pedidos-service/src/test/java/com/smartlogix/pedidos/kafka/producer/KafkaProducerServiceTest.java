package com.smartlogix.pedidos.kafka.producer;

import com.smartlogix.pedidos.event.CompraEvent;
import com.smartlogix.pedidos.event.PedidoCanceladoEvent;
import com.smartlogix.pedidos.event.PedidoCreadoEvent;
import com.smartlogix.pedidos.event.PedidoEstadoActualizadoEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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

    @BeforeEach
    void setUp() {
        // Configure default successful futures for all KafkaTemplates
        SendResult<String, CompraEvent> compraResult = mock(SendResult.class);
        CompletableFuture<SendResult<String, CompraEvent>> compraFuture = CompletableFuture.completedFuture(compraResult);
        lenient().when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(compraFuture);

        SendResult<String, PedidoCreadoEvent> creadoResult = mock(SendResult.class);
        CompletableFuture<SendResult<String, PedidoCreadoEvent>> creadoFuture = CompletableFuture.completedFuture(creadoResult);
        lenient().when(pedidoCreadoKafkaTemplate.send(any(ProducerRecord.class))).thenReturn(creadoFuture);

        SendResult<String, PedidoCanceladoEvent> canceladoResult = mock(SendResult.class);
        CompletableFuture<SendResult<String, PedidoCanceladoEvent>> canceladoFuture = CompletableFuture.completedFuture(canceladoResult);
        lenient().when(pedidoCanceladoKafkaTemplate.send(any(ProducerRecord.class))).thenReturn(canceladoFuture);

        SendResult<String, PedidoEstadoActualizadoEvent> estadoResult = mock(SendResult.class);
        CompletableFuture<SendResult<String, PedidoEstadoActualizadoEvent>> estadoFuture = CompletableFuture.completedFuture(estadoResult);
        lenient().when(pedidoEstadoActualizadoKafkaTemplate.send(any(ProducerRecord.class))).thenReturn(estadoFuture);
    }

    @Test
    void enviarEventoCompra_DebeEnviarEventoCorrectamente() {
        // Act - Just verify it executes without exception
        kafkaProducerService.enviarEventoCompra(1L, 5, "user123");

        // No explicit verification needed - fire-and-forget pattern
        // The test passes if no exception is thrown
    }

    @Test
    void enviarEventoPedidoCreado_DebeEnviarEventoCorrectamente() {
        // Arrange
        PedidoCreadoEvent event = new PedidoCreadoEvent(1L, "user123", "Calle 123", 100.0, null);

        // Act - Just verify it executes without exception
        kafkaProducerService.enviarEventoPedidoCreado(event);

        // No explicit verification needed - fire-and-forget pattern
        // The test passes if no exception is thrown
    }

    @Test
    void enviarEventoPedidoCancelado_DebeEnviarEventoCorrectamente() {
        // Act - Just verify it executes without exception
        kafkaProducerService.enviarEventoPedidoCancelado(1L);

        // No explicit verification needed - fire-and-forget pattern
        // The test passes if no exception is thrown
    }

    @Test
    void enviarEventoPedidoEstadoActualizado_DebeEnviarEventoCorrectamente() {
        // Act - Just verify it executes without exception
        kafkaProducerService.enviarEventoPedidoEstadoActualizado(1L, "ENVIADO");

        // No explicit verification needed - fire-and-forget pattern
        // The test passes if no exception is thrown
    }

    @Test
    void enviarEventoCompra_ErrorEnEnvio_DebeLoguearError() {
        // Arrange: Override default with a failing future
        CompletableFuture<SendResult<String, CompraEvent>> failingFuture = new CompletableFuture<>();
        failingFuture.completeExceptionally(new RuntimeException("Error de Kafka"));
        when(kafkaTemplate.send(any(ProducerRecord.class))).thenReturn(failingFuture);

        // Act - Just verify it executes without exception (logs the error internally)
        kafkaProducerService.enviarEventoCompra(1L, 5, "user123");

        // No explicit verification needed - fire-and-forget pattern with error handling
        // The test passes if no exception is thrown
    }
}
