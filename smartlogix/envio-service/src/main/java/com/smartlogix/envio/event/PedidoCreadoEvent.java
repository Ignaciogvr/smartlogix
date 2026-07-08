package com.smartlogix.envio.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoCreadoEvent {

    private Long pedidoId;

    // 🔥 CAMBIO CLAVE
    private String usuarioId;

    private String direccionDestino;
    private Double total;

    private List<ProductoEvent> productos;
}