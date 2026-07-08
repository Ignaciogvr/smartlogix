package com.smartlogix.bff.service.impl;

import com.smartlogix.bff.aggregator.TrackingAggregator;
import com.smartlogix.bff.client.EnvioClient;
import com.smartlogix.bff.dto.request.ActualizarEstadoEnvioRequest;
import com.smartlogix.bff.dto.request.AsignarChoferRequest;
import com.smartlogix.bff.dto.request.CrearEnvioRequest;
import com.smartlogix.bff.dto.response.EnvioResponse;
import com.smartlogix.bff.dto.response.TrackingResponse;
import com.smartlogix.bff.security.SecurityUtils;
import com.smartlogix.bff.service.EnvioBffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class EnvioBffServiceImpl implements EnvioBffService {

    private static final Logger log = LoggerFactory.getLogger(EnvioBffServiceImpl.class);

    private final EnvioClient envioClient;
    private final TrackingAggregator aggregator;

    public EnvioBffServiceImpl(EnvioClient envioClient, TrackingAggregator aggregator) {
        this.envioClient = envioClient;
        this.aggregator = aggregator;
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
        return aggregator.tracking(trackingCode);
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

    @Override
    public List<EnvioResponse> enviosPorPedido(Long pedidoId) {
        List<EnvioResponse> list = envioClient.enviosPorPedido(pedidoId);
        return list != null ? list : Collections.emptyList();
    }

    @Override
    public EnvioResponse asignarChofer(Long envioId, AsignarChoferRequest request) {
        
        log.info("[BFF] Asignando chofer al envío: envioId={}, choferId={}", 
                envioId, request.getChoferId());
        
        EnvioResponse response = envioClient.asignarChofer(envioId, request);
        
        log.info("[BFF] Chofer asignado correctamente: envioId={}, nuevoEstado={}", 
                envioId, response.getEstado());
        
        return response;
    }
    
    @Override
    public TrackingResponse obtenerTrackingPorId(Long envioId) {
        // Obtener el envío primero para conseguir su tracking code
        EnvioResponse envio = envioClient.obtenerEnvio(envioId);
        if (envio == null || envio.getTrackingCode() == null) {
            throw new RuntimeException("Envío no encontrado o sin código de tracking");
        }
        return aggregator.tracking(envio.getTrackingCode());
    }
    
    @Override
    public Object cotizarEnvio(String region, Double peso) {
        return envioClient.cotizarEnvio(region, peso);
    }
}

