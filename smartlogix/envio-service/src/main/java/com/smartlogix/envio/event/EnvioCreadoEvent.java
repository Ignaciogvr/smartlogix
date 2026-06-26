package com.smartlogix.envio.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnvioCreadoEvent {
    private Long envioId;
    private Long pedidoId;
    private String trackingCode;
}