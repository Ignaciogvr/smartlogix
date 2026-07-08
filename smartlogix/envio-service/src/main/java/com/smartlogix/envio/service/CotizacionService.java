package com.smartlogix.envio.service;

import com.smartlogix.envio.dto.request.CotizacionEnvioRequest;
import com.smartlogix.envio.dto.response.CotizacionResponse;

public interface CotizacionService {

    CotizacionResponse calcularCotizacion(CotizacionEnvioRequest request);
}