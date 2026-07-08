package com.smartlogix.envio.strategy;

import com.smartlogix.envio.model.Envio;
import com.smartlogix.envio.model.EstadoEnvio;

public class EntregadoStrategy implements EstadoEnvioStrategy {

    @Override
    public void procesar(Envio envio) {
        envio.setEstado(EstadoEnvio.ENTREGADO);
    }

    @Override
    public String estado() {
        return "ENTREGADO";
    }
}