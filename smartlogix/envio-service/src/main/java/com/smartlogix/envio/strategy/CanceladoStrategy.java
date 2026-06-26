package com.smartlogix.envio.strategy;

import com.smartlogix.envio.model.Envio;
import com.smartlogix.envio.model.EstadoEnvio;

public class CanceladoStrategy implements EstadoEnvioStrategy {

    @Override
    public void procesar(Envio envio) {
        envio.setEstado(EstadoEnvio.CANCELADO);
    }

    @Override
    public String estado() {
        return "CANCELADO";
    }
}