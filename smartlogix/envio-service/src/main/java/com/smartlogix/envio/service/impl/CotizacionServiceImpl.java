package com.smartlogix.envio.service.impl;

import com.smartlogix.envio.dto.request.CotizacionEnvioRequest;
import com.smartlogix.envio.dto.response.CotizacionResponse;
import com.smartlogix.envio.service.CotizacionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CotizacionServiceImpl implements CotizacionService {

    @Override
    public CotizacionResponse calcularCotizacion(CotizacionEnvioRequest request) {

        BigDecimal base = BigDecimal.valueOf(2500);
        BigDecimal porKg = BigDecimal.valueOf(120);

        BigDecimal total = base.add(
                request.getPeso().multiply(porKg)
        );

        CotizacionResponse response = new CotizacionResponse();
        response.setCosto(total);
        response.setTiempoEstimadoDias(3);

        return response;
    }
}