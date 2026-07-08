package com.smartlogix.envio.service.impl;

import com.smartlogix.envio.model.*;
import com.smartlogix.envio.repository.*;
import com.smartlogix.envio.service.TrackingAvanzadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackingAvanzadoServiceImpl implements TrackingAvanzadoService {

    private final DetalleEnvioMejoradoRepository detalleEnvioMejoradoRepository;
    private final EventoTrackingDetalladoRepository eventoTrackingDetalladoRepository;
    private final IntentoEntregaRepository intentoEntregaRepository;
    private final ReclamoLogisticaRepository reclamoLogisticaRepository;

    @Override
    public DetalleEnvioMejorado crearDetalleEnvio(DetalleEnvioMejorado detalle) {
        return detalleEnvioMejoradoRepository.save(detalle);
    }

    @Override
    public DetalleEnvioMejorado obtenerDetalleEnvio(Long envioId) {
        return detalleEnvioMejoradoRepository.findByEnvioId(envioId).orElse(null);
    }

    @Override
    public EventoTrackingDetallado registrarEventoTracking(EventoTrackingDetallado evento) {
        return eventoTrackingDetalladoRepository.save(evento);
    }

    @Override
    public List<EventoTrackingDetallado> listarEventosPorEnvio(Long envioId) {
        return eventoTrackingDetalladoRepository.findByEnvioIdOrderByTimestampEventoDesc(envioId);
    }

    @Override
    public IntentoEntrega registrarIntentoEntrega(IntentoEntrega intento) {
        return intentoEntregaRepository.save(intento);
    }

    @Override
    public List<IntentoEntrega> listarIntentosPorEnvio(Long envioId) {
        return intentoEntregaRepository.findByEnvioId(envioId);
    }

    @Override
    public ReclamoLogistica registrarReclamo(ReclamoLogistica reclamo) {
        return reclamoLogisticaRepository.save(reclamo);
    }

    @Override
    public List<ReclamoLogistica> listarReclamosPorEnvio(Long envioId) {
        return reclamoLogisticaRepository.findByEnvioId(envioId);
    }
}
