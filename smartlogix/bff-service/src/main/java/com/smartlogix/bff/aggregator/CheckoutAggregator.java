package com.smartlogix.bff.aggregator;

import com.smartlogix.bff.client.EnvioClient;
import com.smartlogix.bff.dto.request.CrearEnvioRequest;
import com.smartlogix.bff.dto.response.EnvioResponse;
import org.springframework.stereotype.Component;

@Component
public class CheckoutAggregator {

    private final EnvioClient envioClient;

    public CheckoutAggregator(EnvioClient envioClient) {
        this.envioClient = envioClient;
    }

    public EnvioResponse crearEnvio(Long pedidoId, String direccionDestino, String usuarioId) {

        CrearEnvioRequest request = new CrearEnvioRequest();
        request.setPedidoId(pedidoId);
        request.setDireccionDestino(direccionDestino);
        request.setUsuarioId(usuarioId);

        return envioClient.crearEnvio(request);
    }
}