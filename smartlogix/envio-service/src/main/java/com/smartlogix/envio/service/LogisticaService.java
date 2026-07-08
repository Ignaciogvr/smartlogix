package com.smartlogix.envio.service;

import com.smartlogix.envio.model.Transportista;
import com.smartlogix.envio.model.RutaTransporte;
import com.smartlogix.envio.model.AsignacionChofer;
import com.smartlogix.envio.model.OptimizacionRuta;

import java.util.List;

public interface LogisticaService {
    Transportista crearTransportista(Transportista transportista);
    List<Transportista> listarTransportistas();
    Transportista obtenerTransportista(Long id);

    RutaTransporte crearRutaTransporte(RutaTransporte rutaTransporte);
    List<RutaTransporte> listarRutasPorTransportista(Long transportistaId);

    AsignacionChofer asignarChofer(AsignacionChofer asignacion);
    AsignacionChofer obtenerAsignacionPorEnvio(Long envioId);

    OptimizacionRuta registrarOptimizacion(OptimizacionRuta optimizacion);
    List<OptimizacionRuta> listarOptimizaciones();
}
