package com.smartlogix.pedidos.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnvioEntregadoEvent {
    private Long envioId;
    private Long pedidoId;
    private String fechaEntrega;
}
