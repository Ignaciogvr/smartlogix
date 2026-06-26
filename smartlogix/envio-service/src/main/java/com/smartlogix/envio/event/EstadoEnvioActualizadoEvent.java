package com.smartlogix.envio.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadoEnvioActualizadoEvent {
    private Long envioId;
    private String estado;
}