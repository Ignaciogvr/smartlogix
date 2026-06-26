package com.smartlogix.envio.strategy;

import com.smartlogix.envio.model.Envio;

public interface EstadoEnvioStrategy {

    void procesar(Envio envio);

    String estado();
}