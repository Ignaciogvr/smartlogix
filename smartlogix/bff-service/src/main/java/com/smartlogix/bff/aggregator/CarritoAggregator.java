package com.smartlogix.bff.aggregator;

import com.smartlogix.bff.client.InventoryClient;
import com.smartlogix.bff.client.PedidoClient;
import com.smartlogix.bff.dto.response.CarritoEnriquecidoDTO;
import com.smartlogix.bff.dto.response.CarritoItemEnriquecidoDTO;
import com.smartlogix.bff.dto.response.ProductoCatalogoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CarritoAggregator {

    private static final Logger log = LoggerFactory.getLogger(CarritoAggregator.class);

    private final PedidoClient pedidoClient;
    private final InventoryClient inventoryClient;

    public CarritoAggregator(PedidoClient pedidoClient, InventoryClient inventoryClient) {
        this.pedidoClient = pedidoClient;
        this.inventoryClient = inventoryClient;
    }

    public CarritoEnriquecidoDTO obtenerCarritoEnriquecido(String userId) {
        Object carritoResponse = pedidoClient.obtenerCarrito(userId);
        
        if (carritoResponse == null) {
            return new CarritoEnriquecidoDTO(new ArrayList<>(), BigDecimal.ZERO, BigDecimal.ZERO);
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> responseMap = (Map<String, Object>) carritoResponse;
        
        Object dataObj = responseMap.get("data");
        if (dataObj == null) {
            return new CarritoEnriquecidoDTO(new ArrayList<>(), BigDecimal.ZERO, BigDecimal.ZERO);
        }
        
        @SuppressWarnings("unchecked")
        Map<String, Object> carritoData = (Map<String, Object>) dataObj;
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) carritoData.get("items");
        
        if (items == null || items.isEmpty()) {
            return new CarritoEnriquecidoDTO(new ArrayList<>(), BigDecimal.ZERO, BigDecimal.ZERO);
        }

        List<CarritoItemEnriquecidoDTO> itemsEnriquecidos = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (Map<String, Object> item : items) {
            try {
                Long productoId = getLongFromMap(item, "productoId");
                Integer cantidad = getIntegerFromMap(item, "cantidad");
                BigDecimal precio = getBigDecimalFromMap(item, "precio");

                var productoEnvelope = inventoryClient.obtenerProducto(productoId);
                
                if (productoEnvelope == null || productoEnvelope.getData() == null) {
                    log.error("No se pudo obtener información del producto {} desde el microservicio de inventario", productoId);
                    throw new RuntimeException("Error al obtener información del producto " + productoId + " desde el microservicio de inventario");
                }
                
                ProductoCatalogoDTO producto = productoEnvelope.getData();
                BigDecimal subtotalItem = precio.multiply(BigDecimal.valueOf(cantidad));
                
                CarritoItemEnriquecidoDTO itemEnriquecido = new CarritoItemEnriquecidoDTO(
                    productoId,
                    producto.getNombre(),
                    producto.getDescripcion(),
                    producto.getCategoria(),
                    producto.getImagenPrincipal(),
                    cantidad,
                    precio,
                    subtotalItem
                );
                
                itemsEnriquecidos.add(itemEnriquecido);
                subtotal = subtotal.add(subtotalItem);
            } catch (Exception e) {
                log.error("Error enriqueciendo item del carrito", e);
            }
        }

        return new CarritoEnriquecidoDTO(itemsEnriquecidos, subtotal, subtotal);
    }

    private Long getLongFromMap(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return Long.parseLong(value.toString());
    }

    private Integer getIntegerFromMap(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return Integer.parseInt(value.toString());
    }

    private BigDecimal getBigDecimalFromMap(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        return new BigDecimal(value.toString());
    }
    
    public Map<String, Object> enriquecerItemConPrecio(Long productoId, Integer cantidad) {
        // Obtener el producto del inventory-service
        var productoEnvelope = inventoryClient.obtenerProducto(productoId);
        
        if (productoEnvelope == null || productoEnvelope.getData() == null) {
            log.error("No se pudo obtener el producto {} para agregar al carrito", productoId);
            throw new RuntimeException("Producto no encontrado: " + productoId);
        }
        
        ProductoCatalogoDTO producto = productoEnvelope.getData();
        
        return Map.of(
            "productoId", productoId,
            "cantidad", cantidad,
            "precio", producto.getPrecio()
        );
    }
}
