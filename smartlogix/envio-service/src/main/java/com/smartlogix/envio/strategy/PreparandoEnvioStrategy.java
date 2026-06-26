package com.smartlogix.envio.strategy;

import com.smartlogix.envio.model.Envio;
import com.smartlogix.envio.model.EstadoEnvio;

public class PreparandoEnvioStrategy implements EstadoEnvioStrategy {

    @Override
    public void procesar(Envio envio) {
        envio.setEstado(EstadoEnvio.PREPARANDO);
    }

    @Override
    public String estado() {
        return "PREPARANDO";
    }
}