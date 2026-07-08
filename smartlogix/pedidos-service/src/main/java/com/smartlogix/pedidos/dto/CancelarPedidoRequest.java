package com.smartlogix.pedidos.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class CancelarPedidoRequest {
    @NotBlank(message = "El motivo de cancelación es requerido")
    private String motivo;
}
