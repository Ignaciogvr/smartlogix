package com.smartlogix.envio.service.impl;

import com.smartlogix.envio.model.*;
import com.smartlogix.envio.repository.*;
import com.smartlogix.envio.service.LogisticaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogisticaServiceImpl implements LogisticaService {

    private final TransportistaRepository transportistaRepository;
    private final RutaTransporteRepository rutaTransporteRepository;
    private final AsignacionChoferRepository asignacionChoferRepository;
    private final OptimizacionRutaRepository optimizacionRutaRepository;

    @Override
    public Transportista crearTransportista(Transportista transportista) {
        return transportistaRepository.save(transportista);
    }

    @Override
    public List<Transportista> listarTransportistas() {
        return transportistaRepository.findAll();
    }

    @Override
    public Transportista obtenerTransportista(Long id) {
        return transportistaRepository.findById(id).orElseThrow(() -> new RuntimeException("Transportista no encontrado"));
    }

    @Override
    public RutaTransporte crearRutaTransporte(RutaTransporte rutaTransporte) {
        return rutaTransporteRepository.save(rutaTransporte);
    }

    @Override
    public List<RutaTransporte> listarRutasPorTransportista(Long transportistaId) {
        return rutaTransporteRepository.findByTransportistaId(transportistaId);
    }

    @Override
    public AsignacionChofer asignarChofer(AsignacionChofer asignacion) {
        return asignacionChoferRepository.save(asignacion);
    }

    @Override
    public AsignacionChofer obtenerAsignacionPorEnvio(Long envioId) {
        return asignacionChoferRepository.findByEnvioId(envioId).orElse(null);
    }

    @Override
    public OptimizacionRuta registrarOptimizacion(OptimizacionRuta optimizacion) {
        return optimizacionRutaRepository.save(optimizacion);
    }

    @Override
    public List<OptimizacionRuta> listarOptimizaciones() {
        return optimizacionRutaRepository.findAll();
    }
}
