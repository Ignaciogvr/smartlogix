package com.smartlogix.bff.service.impl;

import com.smartlogix.bff.client.EnvioClient;
import com.smartlogix.bff.dto.request.ActualizarEstadoEnvioRequest;
import com.smartlogix.bff.dto.request.CrearEnvioRequest;
import com.smartlogix.bff.dto.response.EnvioResponse;
import com.smartlogix.bff.dto.response.TrackingResponse;
import com.smartlogix.bff.mapper.TrackingMapper;
import com.smartlogix.bff.security.SecurityUtils;
import com.smartlogix.bff.service.EnvioBffService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class EnvioBffServiceImpl implements EnvioBffService {

    private final EnvioClient envioClient;

    public EnvioBffServiceImpl(EnvioClient envioClient) {
        this.envioClient = envioClient;
    }

    @Override
    public EnvioResponse crearEnvio(CrearEnvioRequest request) {
        if (request.getUsuarioId() == null || request.getUsuarioId().isBlank()) {
            String subject = SecurityUtils.auth0Subject()
                    .orElseThrow(() -> new IllegalStateException("No se pudo resolver el usuario para el envío"));
            request.setUsuarioId(subject);
        }
        return envioClient.crearEnvio(request);
    }

    @Override
    public List<EnvioResponse> listarEnvios() {
        List<EnvioResponse> list = envioClient.listarEnvios();
        return list != null ? list : Collections.emptyList();
    }

    @Override
    public EnvioResponse obtenerEnvio(Long envioId) {
        return envioClient.obtenerEnvio(envioId);
    }

    @Override
    public TrackingResponse obtenerTracking(String trackingCode) {
        EnvioResponse envio = envioClient.obtenerTracking(trackingCode);
        return TrackingMapper.fromEnvio(envio);
    }

    @Override
    public List<EnvioResponse> enviosUsuario(String usuarioId) {
        List<EnvioResponse> list = envioClient.enviosUsuario(usuarioId);
        return list != null ? list : Collections.emptyList();
    }

    @Override
    public EnvioResponse actualizarEstado(
            Long envioId,
            ActualizarEstadoEnvioRequest request
    ) {
        return envioClient.actualizarEstado(envioId, request);
    }
}
