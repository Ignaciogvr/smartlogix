package com.smartlogix.envio.strategy;

import com.smartlogix.envio.model.Envio;
import com.smartlogix.envio.model.EstadoEnvio;

public class PendienteStrategy implements EstadoEnvioStrategy {

    @Override
    public void procesar(Envio envio) {
        envio.setEstado(EstadoEnvio.PENDIENTE);
    }

    @Override
    public String estado() {
        return "PENDIENTE";
    }
}
