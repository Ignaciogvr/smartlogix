package com.smartlogix.envio.service;

import com.smartlogix.envio.model.DetalleEnvioMejorado;
import com.smartlogix.envio.model.EventoTrackingDetallado;
import com.smartlogix.envio.model.IntentoEntrega;
import com.smartlogix.envio.model.ReclamoLogistica;

import java.util.List;

public interface TrackingAvanzadoService {
    DetalleEnvioMejorado crearDetalleEnvio(DetalleEnvioMejorado detalle);
    DetalleEnvioMejorado obtenerDetalleEnvio(Long envioId);

    EventoTrackingDetallado registrarEventoTracking(EventoTrackingDetallado evento);
    List<EventoTrackingDetallado> listarEventosPorEnvio(Long envioId);

    IntentoEntrega registrarIntentoEntrega(IntentoEntrega intento);
    List<IntentoEntrega> listarIntentosPorEnvio(Long envioId);

    ReclamoLogistica registrarReclamo(ReclamoLogistica reclamo);
    List<ReclamoLogistica> listarReclamosPorEnvio(Long envioId);
}
