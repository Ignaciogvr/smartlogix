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
        } else if (actual == EstadoEnvio.PENDIENTE && nuevo != EstadoEnvio.PREPARANDO) {
            throw new IllegalStateException("De PENDIENTE solo puede pasar a PREPARANDO");
        } else if (actual == EstadoEnvio.PREPARANDO && nuevo != EstadoEnvio.EN_RUTA) {
            throw new IllegalStateException("De PREPARANDO solo puede pasar a EN_RUTA");
        } else if (actual == EstadoEnvio.EN_RUTA && nuevo != EstadoEnvio.ENTREGADO) {
            throw new IllegalStateException("De EN_RUTA solo puede pasar a ENTREGADO");
        }

        envio.setEstado(nuevo);

        log.info(
                "📦 Estado actualizado envio={} estado={}",
                envioId,
                nuevoEstado
        );

        return map(
                repo.save(envio)
        );
    }

    @Override
    public void cancelarPorPedido(Long pedidoId) {
        List<Envio> envios = repo.findByPedidoId(pedidoId);
        for (Envio envio : envios) {
            if (envio.getEstado() == EstadoEnvio.PENDIENTE || envio.getEstado() == EstadoEnvio.PREPARANDO) {
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

        r.setFechaCreacion(
                e.getFechaCreacion()
        );

        return r;
    }
}