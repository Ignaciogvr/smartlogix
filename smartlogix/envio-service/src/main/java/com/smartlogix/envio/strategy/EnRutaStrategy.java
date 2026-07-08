package com.smartlogix.envio.strategy;

import com.smartlogix.envio.model.Envio;
import com.smartlogix.envio.model.EstadoEnvio;

public class EnRutaStrategy implements EstadoEnvioStrategy {

    @Override
    public void procesar(Envio envio) {
        envio.setEstado(EstadoEnvio.EN_RUTA);
    }

    @Override
    public String estado() {
        return "EN_RUTA";
    }
}