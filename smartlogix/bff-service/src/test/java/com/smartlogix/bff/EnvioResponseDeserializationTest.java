package com.smartlogix.bff;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.smartlogix.bff.dto.response.EnvioResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EnvioResponseDeserializationTest {

    @Test
    public void testDeserialization() throws Exception {
        String json = "{\"id\": 1, \"pedidoId\": 5, \"trackingId\": \"SLX-123\", \"estado\": \"PENDIENTE\", \"direccionDestino\": \"Calle 1\", \"fechaCreacion\": \"2026-06-01T12:00:00\"}";
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        EnvioResponse response = mapper.readValue(json, EnvioResponse.class);
        
        System.out.println("envioId: " + response.getEnvioId());
        System.out.println("trackingCode: " + response.getTrackingCode());
        System.out.println("fechaEnvio: " + response.getFechaEnvio());
        
        assertNotNull(response.getEnvioId(), "envioId is null");
        assertNotNull(response.getTrackingCode(), "trackingCode is null");
        assertNotNull(response.getFechaEnvio(), "fechaEnvio is null");
    }
}
