package com.smartlogix.pedidos.repository;

import com.smartlogix.pedidos.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    /**
     * Verifica si un usuario compró un producto en algún pedido entregado.
     * Usado para la validación "Solo compradores pueden comentar".
     */
    @Query("SELECT COUNT(d) > 0 FROM DetallePedido d " +
           "JOIN d.pedido p " +
           "WHERE p.usuarioId = :usuarioId " +
           "AND d.productoId = :productoId " +
           "AND p.estado = 'DELIVERED'")
    boolean verificarCompraEntregada(@Param("usuarioId") String usuarioId,
                                     @Param("productoId") Long productoId);

    /**
     * Recomendaciones: productos comprados junto con el producto dado.
     * "Usuarios que compraron esto también compraron..."
     */
    @Query("SELECT d2.productoId FROM DetallePedido d1 " +
           "JOIN d1.pedido p " +
           "JOIN DetallePedido d2 ON d2.pedido = p " +
           "WHERE d1.productoId = :productoId " +
           "AND d2.productoId <> :productoId " +
           "GROUP BY d2.productoId " +
           "ORDER BY COUNT(d2.productoId) DESC")
    List<Long> findFrequentlyBoughtTogether(@Param("productoId") Long productoId);

    /**
     * Productos más vendidos por cantidad total vendida.
     */
    @Query("SELECT d.productoId, SUM(d.cantidad) as totalVendido FROM DetallePedido d " +
           "JOIN d.pedido p " +
           "WHERE p.estado = 'DELIVERED' " +
           "GROUP BY d.productoId " +
           "ORDER BY totalVendido DESC")
    List<Object[]> findTopProductos();

    /**
     * Total de ingresos de pedidos entregados.
     */
    @Query("SELECT COALESCE(SUM(d.precio * d.cantidad), 0) FROM DetallePedido d " +
           "JOIN d.pedido p WHERE p.estado = 'DELIVERED'")
    Double calcularIngresosTotales();
}