package com.smartlogix.pedidos.service.impl;

import com.smartlogix.pedidos.dto.PedidoDTO;
import com.smartlogix.pedidos.model.*;
import com.smartlogix.pedidos.repository.PedidoRepository;
import com.smartlogix.pedidos.service.PedidoService;
import com.smartlogix.pedidos.producer.KafkaProducer;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final WebClient webClient;
    private final KafkaProducer producer;

    private static final String INVENTORY_SERVICE = "http://localhost:8081";
    private static final String USER_SERVICE = "http://localhost:8080";

    public PedidoServiceImpl(PedidoRepository pedidoRepository,
                             WebClient.Builder webClientBuilder,
                             KafkaProducer producer) {
        this.pedidoRepository = pedidoRepository;
        this.webClient = webClientBuilder.build();
        this.producer = producer;
    }

    @Override
    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    @Override
    public Pedido crearDesdeDTO(PedidoDTO dto) {

        if (dto.getUsuarioId() == null || dto.getUsuarioId().isBlank()) {
            throw new IllegalArgumentException("usuarioId obligatorio");
        }

        validarUsuario(dto.getUsuarioId());

        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("Debe tener productos");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuarioId(dto.getUsuarioId());

        double total = dto.getDetalles().stream().mapToDouble(d -> {

            validarDetalle(d);
            validarStock(d);

            DetallePedido det = new DetallePedido();
            det.setProductoId(d.getProductoId());
            det.setCantidad(d.getCantidad());
            det.setPrecio(d.getPrecio());
            det.setPedido(pedido);

            pedido.getDetalles().add(det);

            return d.getCantidad() * d.getPrecio();
        }).sum();

        pedido.setTotal(total);

        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido obtener(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));
    }

    @Override
    public Pedido actualizarDesdeDTO(Long id, PedidoDTO dto) {

        Pedido pedido = obtener(id);

        pedido.getDetalles().clear();

        double total = dto.getDetalles().stream().mapToDouble(d -> {

            validarDetalle(d);
            validarStock(d);

            DetallePedido det = new DetallePedido();
            det.setProductoId(d.getProductoId());
            det.setCantidad(d.getCantidad());
            det.setPrecio(d.getPrecio());
            det.setPedido(pedido);

            pedido.getDetalles().add(det);

            return d.getCantidad() * d.getPrecio();
        }).sum();

        pedido.setTotal(total);

        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido cerrar(Long id) {

        Pedido pedido = obtener(id);
        pedido.setEstado(EstadoPedido.CERRADO);

        Pedido saved = pedidoRepository.save(pedido);

        saved.getDetalles().forEach(d -> {
            try {
                producer.enviarEventoCompra(
                        d.getProductoId(),
                        d.getCantidad(),
                        saved.getUsuarioId()
                );
            } catch (Exception ignored) {}
        });

        return saved;
    }

    @Override
    public void eliminar(Long id) {
        Pedido pedido = obtener(id);
        pedido.setEstado(EstadoPedido.CERRADO);
        pedidoRepository.save(pedido);
    }

    @Override
    public List<Pedido> porUsuario(String usuarioId) {
        return pedidoRepository.findByUsuarioId(usuarioId);
    }

    private void validarDetalle(PedidoDTO.DetalleDTO d) {
        if (d.getProductoId() == null ||
            d.getCantidad() == null ||
            d.getCantidad() <= 0 ||
            d.getPrecio() == null ||
            d.getPrecio() <= 0) {
            throw new IllegalArgumentException("Detalle inválido");
        }
    }

    private void validarUsuario(String usuarioId) {
        Boolean ok = webClient.get()
                .uri(USER_SERVICE + "/usuarios/exists/" + usuarioId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (!Boolean.TRUE.equals(ok)) {
            throw new EntityNotFoundException("Usuario no existe");
        }
    }

    private void validarStock(PedidoDTO.DetalleDTO d) {
        Boolean ok = webClient.get()
                .uri(INVENTORY_SERVICE + "/productos/" +
                        d.getProductoId() + "/validar/" + d.getCantidad())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (!Boolean.TRUE.equals(ok)) {
            throw new IllegalStateException("Stock insuficiente");
        }
    }
}