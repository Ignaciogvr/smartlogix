package com.smartlogix.pedidos.service.impl;

import com.smartlogix.pedidos.kafka.producer.KafkaProducerService;
import com.smartlogix.pedidos.model.Devolucion;
import com.smartlogix.pedidos.model.EstadoDevolucion;
import com.smartlogix.pedidos.repository.DevolucionRepository;
import com.smartlogix.pedidos.service.DevolucionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DevolucionServiceImpl implements DevolucionService {

    private final DevolucionRepository devolucionRepository;
    private final KafkaProducerService producer;

    @Autowired
    public DevolucionServiceImpl(DevolucionRepository devolucionRepository,
                                  KafkaProducerService producer) {
        this.devolucionRepository = devolucionRepository;
        this.producer = producer;
    }

    @Override
    @Transactional
    public Devolucion crearDevolucion(Devolucion devolucion) {
        Devolucion saved = devolucionRepository.save(devolucion);
        producer.enviarEventoDevolucion(
            "DevolucionCreada",
            saved.getId(),
            saved.getPedido() != null ? saved.getPedido().getId() : null,
            saved.getUsuarioId(),
            saved.getEstadoDevolucion().name()
        );
        return saved;
    }

    @Override
    public Optional<Devolucion> obtenerDevolucionPorId(Long id) {
        return devolucionRepository.findById(id);
    }

    @Override
    public List<Devolucion> obtenerDevolucionesPorPedido(Long pedidoId) {
        return devolucionRepository.findByPedidoId(pedidoId);
    }

    @Override
    public List<Devolucion> obtenerDevolucionesPorUsuario(String usuarioId) {
        return devolucionRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<Devolucion> obtenerTodasLasDevoluciones() {
        return devolucionRepository.findAll();
    }

    @Override
    @Transactional
    public Devolucion actualizarEstadoDevolucion(Long id, EstadoDevolucion nuevoEstado, String observaciones) {
        Optional<Devolucion> devolucionOpt = devolucionRepository.findById(id);
        if (devolucionOpt.isPresent()) {
            Devolucion devolucion = devolucionOpt.get();
            devolucion.setEstadoDevolucion(nuevoEstado);
            if (observaciones != null && !observaciones.trim().isEmpty()) {
                devolucion.setObservacionesInspeccion(observaciones);
            }
            
            // Determinar tipo de evento según el nuevo estado
            LocalDateTime now = LocalDateTime.now();
            String eventType;
            switch (nuevoEstado) {
                case RECIBIDA:   eventType = "DevolucionRecibida"; devolucion.setFechaRecepcion(now); break;
                case EVALUANDO:  eventType = "DevolucionEvaluando"; devolucion.setFechaInspeccion(now); break;
                case COMPLETADA: eventType = "DevolucionAprobada"; devolucion.setFechaResolucion(now); break;
                case RECHAZADA:  eventType = "DevolucionRechazada"; devolucion.setFechaResolucion(now); break;
                default:         eventType = "DevolucionEstadoActualizado";
            }
            Devolucion actualizada = devolucionRepository.save(devolucion);
            producer.enviarEventoDevolucion(
                eventType,
                actualizada.getId(),
                actualizada.getPedido() != null ? actualizada.getPedido().getId() : null,
                actualizada.getUsuarioId(),
                actualizada.getEstadoDevolucion().name()
            );
            return actualizada;
        }
        throw new RuntimeException("Devolución no encontrada con ID: " + id);
    }

    @Override
    @Transactional
    public void eliminarDevolucion(Long id) {
        devolucionRepository.deleteById(id);
    }
}
