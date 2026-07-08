package com.smartlogix.inventory.kafka.producer;

import com.smartlogix.inventory.event.ProductoCreadoEvent;
import com.smartlogix.inventory.event.StockDescontadoEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import org.apache.kafka.clients.producer.ProducerRecord;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
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
        verify(kafkaTemplate, times(1)).send((ProducerRecord<String, Object>) org.mockito.ArgumentMatchers.<ProducerRecord<String, Object>>argThat(record -> 
                record != null && 
                "producto-creado".equals(record.topic()) &&
                event.equals(record.value())
        ));
    }

    @Test
    void enviarProductoCreado_EventoNulo_NoDebeLlamarSend() {
        // Act
        kafkaProducer.enviarProductoCreado(null);

        // Assert
        verify(kafkaTemplate, never()).send(any(ProducerRecord.class));
    }

    @Test
    void enviarProductoCreado_EventoSinId_NoDebeLlamarSend() {
        // Arrange
        ProductoCreadoEvent event = new ProductoCreadoEvent(null, "Laptop", "Electronics", 1500.0, 10, null);

        // Act
        kafkaProducer.enviarProductoCreado(event);

        // Assert
        verify(kafkaTemplate, never()).send(any(ProducerRecord.class));
    }

    @Test
    void enviarStockDescontado_EventoValido_DebeLlamarSend() {
        // Arrange
        StockDescontadoEvent event = new StockDescontadoEvent(1L, 5, "user123");

        // Act
        kafkaProducer.enviarStockDescontado(event);

        // Assert
        verify(kafkaTemplate, times(1)).send((ProducerRecord<String, Object>) org.mockito.ArgumentMatchers.<ProducerRecord<String, Object>>argThat(record -> 
                record != null && 
                "stock-descontado".equals(record.topic()) &&
                event.equals(record.value())
        ));
    }

    @Test
    void enviarStockDescontado_EventoNulo_NoDebeLlamarSend() {
        // Act
        kafkaProducer.enviarStockDescontado(null);

        // Assert
        verify(kafkaTemplate, never()).send(any(ProducerRecord.class));
    }

    @Test
    void enviarStockDescontado_EventoSinProductoId_NoDebeLlamarSend() {
        // Arrange
        StockDescontadoEvent event = new StockDescontadoEvent(null, 5, "user123");

        // Act
        kafkaProducer.enviarStockDescontado(event);

        // Assert
        verify(kafkaTemplate, never()).send(any(ProducerRecord.class));
    }
}
