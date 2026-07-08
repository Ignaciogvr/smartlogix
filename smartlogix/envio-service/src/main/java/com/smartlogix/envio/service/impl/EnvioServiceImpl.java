package com.smartlogix.envio.service.impl;

import com.smartlogix.envio.client.PedidoClient;
import com.smartlogix.envio.client.UsuarioClient;
import com.smartlogix.envio.dto.request.CrearEnvioRequest;
import com.smartlogix.envio.dto.response.EnvioResponse;
import com.smartlogix.envio.event.EnvioCreadoEvent;
import com.smartlogix.envio.kafka.producer.KafkaProducerService;
import com.smartlogix.envio.model.Envio;
import com.smartlogix.envio.model.EstadoEnvio;
import com.smartlogix.envio.repository.EnvioRepository;
import com.smartlogix.envio.service.EnvioService;
import com.smartlogix.envio.util.TrackingGenerator;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EnvioServiceImpl implements EnvioService {

    private final EnvioRepository repo;
    private final PedidoClient pedidoClient;
    private final UsuarioClient usuarioClient;
    private final KafkaProducerService kafka;

    public EnvioServiceImpl(
            EnvioRepository repo,
            PedidoClient pedidoClient,
            UsuarioClient usuarioClient,
            KafkaProducerService kafka
    ) {
        this.repo = repo;
        this.pedidoClient = pedidoClient;
        this.usuarioClient = usuarioClient;
        this.kafka = kafka;
    }

    @Override
    public EnvioResponse crearEnvio(
            CrearEnvioRequest request
    ) {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String token =
                ((JwtAuthenticationToken) auth)
                        .getToken()
                        .getTokenValue();

        token = "Bearer " + token;

        log.info(
                "🚀 Crear envío pedidoId={} usuarioId={}",
                request.getPedidoId(),
                request.getUsuarioId()
        );

        // VALIDAR PEDIDO
        try {

            pedidoClient.obtenerPedido(
                    request.getPedidoId().toString()
            );

            log.info("✅ Pedido validado");

        } catch (Exception e) {

            log.error("❌ PedidoService ERROR", e);

            throw new RuntimeException(
                    "PedidoService no disponible"
            );
        }

        // VALIDAR USUARIO
        try {

            Boolean existeUsuario =
                    usuarioClient.obtenerUsuarioPorId(
                            request.getUsuarioId()
                    );

            if (Boolean.FALSE.equals(existeUsuario)) {

                throw new RuntimeException(
                        "Usuario no existe"
                );
            }

            log.info("✅ Usuario validado");

        } catch (Exception e) {

            log.error("❌ UsuarioService ERROR", e);

            throw new RuntimeException(
                    "UsuarioService no disponible"
            );
        }

        // CREAR ENVÍO
        Envio envio = new Envio();

        envio.setPedidoId(request.getPedidoId());
        envio.setUsuarioId(request.getUsuarioId());
        envio.setTrackingNumber(
                TrackingGenerator.generate()
        );

        envio.setEstado(
                EstadoEnvio.PENDIENTE
        );

        envio.setDireccionDestino(
                request.getDireccionDestino()
        );

        Envio saved = repo.save(envio);

        // EVENTO KAFKA
        kafka.enviarEventoEnvio(
                new EnvioCreadoEvent(
                        saved.getId(),
                        saved.getPedidoId(),
                        saved.getTrackingNumber()
                )
        );

        log.info(
                "📤 Evento envío publicado tracking={}",
                saved.getTrackingNumber()
        );

        return map(saved);
    }

    @Override
    public EnvioResponse crearEnvioInterno(Long pedidoId, String usuarioId, String direccionDestino) {
        log.info("🚀 Crear envío automático pedidoId={} usuarioId={}", pedidoId, usuarioId);

        Envio envio = new Envio();
        envio.setPedidoId(pedidoId);
        envio.setUsuarioId(usuarioId);
        envio.setTrackingNumber(TrackingGenerator.generate());
        envio.setEstado(EstadoEnvio.PENDIENTE);
        envio.setDireccionDestino(direccionDestino);

        Envio saved = repo.save(envio);

        kafka.enviarEventoEnvio(
                new EnvioCreadoEvent(
                        saved.getId(),
                        saved.getPedidoId(),
                        saved.getTrackingNumber()
                )
        );

        log.info("📤 Evento envío publicado tracking={}", saved.getTrackingNumber());
        return map(saved);
    }

    // NUEVO

    @Override
    public EnvioResponse obtenerPorId(
            Long id
    ) {

        Envio envio = repo.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Envío no encontrado"
                        )
                );

        return map(envio);
    }

    @Override
    public EnvioResponse obtenerPorTracking(
            String trackingNumber
    ) {

        return map(
                repo.findByTrackingNumber(trackingNumber)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Tracking no encontrado"
                                )
                        )
        );
    }

    @Override
    public List<EnvioResponse> obtenerPorPedido(
            Long pedidoId
    ) {

        return repo.findByPedidoId(pedidoId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<EnvioResponse> listarTodos() {
        return repo.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<EnvioResponse> listarPorUsuario(String usuarioId) {
        return repo.findByUsuarioId(usuarioId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<EnvioResponse> listarPorChofer(String choferId) {
        return repo.findByChoferId(choferId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public EnvioResponse actualizarEstado(
            Long envioId,
            String nuevoEstado
    ) {

        Envio envio = repo.findById(envioId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Envío no encontrado"
                        )
                );

        EstadoEnvio actual = envio.getEstado();
        EstadoEnvio nuevo = EstadoEnvio.valueOf(nuevoEstado.toUpperCase());

        if (nuevo == EstadoEnvio.CANCELADO) {
            if (actual != EstadoEnvio.PENDIENTE && actual != EstadoEnvio.PREPARANDO) {
                throw new IllegalStateException("Solo se puede cancelar un envío PENDIENTE o PREPARANDO");
            }
        } else if (actual == EstadoEnvio.CANCELADO) {
            throw new IllegalStateException("No se puede cambiar el estado de un envío cancelado");
        } else if (actual == EstadoEnvio.ENTREGADO) {
            throw new IllegalStateException("No se puede cambiar el estado de un envío entregado");
        } else if (actual == EstadoEnvio.PENDIENTE && nuevo != EstadoEnvio.PREPARANDO && nuevo != EstadoEnvio.ASIGNADO) {
            throw new IllegalStateException("De PENDIENTE solo puede pasar a PREPARANDO o ASIGNADO");
        } else if (actual == EstadoEnvio.PREPARANDO && nuevo != EstadoEnvio.ASIGNADO && nuevo != EstadoEnvio.EN_RUTA) {
            throw new IllegalStateException("De PREPARANDO solo puede pasar a ASIGNADO o EN_RUTA");
        } else if (actual == EstadoEnvio.ASIGNADO && nuevo != EstadoEnvio.EN_RUTA) {
            throw new IllegalStateException("De ASIGNADO solo puede pasar a EN_RUTA");
        } else if (actual == EstadoEnvio.EN_RUTA && nuevo != EstadoEnvio.ENTREGADO) {
            throw new IllegalStateException("De EN_RUTA solo puede pasar a ENTREGADO");
        }

        envio.setEstado(nuevo);

        log.info(
                "📦 Estado actualizado envio={} estado={}",
                envioId,
                nuevoEstado
        );

        Envio saved = repo.save(envio);
        kafka.enviarTrackingActualizado(saved.getId(), nuevo.name(), null);
        return map(saved);
    }

    @Override
    public EnvioResponse asignarChofer(Long envioId, String choferId, String choferNombre) {
        
        log.info("[ENVIO] Asignando chofer {} al envío {}", choferId, envioId);
        
        // Validar que el envío existe
        Envio envio = repo.findById(envioId)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado con ID: " + envioId));

        // Validar que el envío esté en estado PENDIENTE
        if (envio.getEstado() != EstadoEnvio.PENDIENTE) {
            throw new IllegalStateException(
                "Solo envíos en estado PENDIENTE pueden asignarse a un chofer. Estado actual: " + envio.getEstado()
            );
        }

        // Validar que se proporcione un choferId
        if (choferId == null || choferId.isBlank()) {
            throw new IllegalArgumentException("El choferId no puede estar vacío");
        }

        // Asignar chofer
        envio.setChoferId(choferId);
        if (choferNombre != null && !choferNombre.isBlank()) {
            envio.setChoferNombre(choferNombre);
        }
        
        // Cambiar estado a ASIGNADO automáticamente
        envio.setEstado(EstadoEnvio.ASIGNADO);
        
        Envio saved = repo.save(envio);
        kafka.enviarTransportistaAsignado(saved.getId(), null, choferNombre);

        log.info("[ENVIO] Chofer asignado exitosamente: envioId={}, choferId={}, nuevoEstado={}",
                envioId, choferId, EstadoEnvio.ASIGNADO);

        return map(saved);
    }

    @Override
    public EnvioResponse marcarEntregado(Long id, com.smartlogix.envio.dto.request.EntregarRequest request, String choferId) {
        log.info("[ENVIO] Marcando como ENTREGADO envioId={} choferId={}", id, choferId);

        Envio envio = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado"));

        if (!choferId.equals(envio.getChoferId()) && !SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("No autorizado para este envío");
        }

        if (envio.getEstado() == EstadoEnvio.ENTREGADO) {
            throw new IllegalStateException("El envío ya está entregado");
        }

        envio.setEstado(EstadoEnvio.ENTREGADO);

        // Agregamos el historial con la información del receptor
        String detallesEntrega = String.format("[ENTREGADO] Receptor: %s. Firma: %s. Obs: %s",
                request.getNombreReceptor() != null ? request.getNombreReceptor() : "N/A",
                request.getFirmaReceptor() != null ? "[ADJUNTA]" : "No adjunta",
                request.getObservaciones() != null ? request.getObservaciones() : "Ninguna");

        com.smartlogix.envio.model.HistorialEnvio historial = new com.smartlogix.envio.model.HistorialEnvio(envio, EstadoEnvio.ENTREGADO, detallesEntrega);
        envio.getHistorial().add(historial);

        Envio saved = repo.save(envio);

        // Evento Kafka de Envío Entregado
        kafka.enviarEventoEntregado(new com.smartlogix.envio.event.EnvioEntregadoEvent(
                saved.getId(),
                saved.getPedidoId(),
                java.time.LocalDateTime.now().toString()
        ));

        return map(saved);
    }

    @Override
    public void cancelarPorPedido(Long pedidoId) {
        List<Envio> envios = repo.findByPedidoId(pedidoId);
        for (Envio envio : envios) {
            if (envio.getEstado() == EstadoEnvio.PENDIENTE || envio.getEstado() == EstadoEnvio.PREPARANDO || envio.getEstado() == EstadoEnvio.ASIGNADO) {
                envio.setEstado(EstadoEnvio.CANCELADO);
                repo.save(envio);
                log.info("🚫 Envío cancelado por cancelación de pedido: tracking={}", envio.getTrackingNumber());
            } else {
                log.warn("⚠️ No se pudo cancelar envío (ya está en ruta o entregado): tracking={}", envio.getTrackingNumber());
            }
        }
    }

    private EnvioResponse map(
            Envio e
    ) {

        EnvioResponse r =
                new EnvioResponse();

        r.setId(e.getId());
        r.setPedidoId(e.getPedidoId());
        r.setTrackingId(
                e.getTrackingNumber()
        );

        r.setEstado(
                e.getEstado().name()
        );

        r.setDireccionDestino(
                e.getDireccionDestino()
        );
        r.setChoferId(e.getChoferId());
        r.setChoferNombre(e.getChoferNombre());

        r.setFechaCreacion(
                e.getFechaCreacion()
        );
        // New fields for delivery proof
        r.setFotoEntregaUrl(e.getFotoEntregaUrl());
        r.setFirmaReceptorUrl(e.getFirmaReceptorUrl());

        return r;
    }
}