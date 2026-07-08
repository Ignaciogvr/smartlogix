package com.smartlogix.bff.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class CarritoEnriquecidoDTO {
    private List<CarritoItemEnriquecidoDTO> items;
    private BigDecimal subtotal;
    private BigDecimal total;

    public CarritoEnriquecidoDTO() {
    }

    public CarritoEnriquecidoDTO(List<CarritoItemEnriquecidoDTO> items, BigDecimal subtotal, BigDecimal total) {
        this.items = items;
        this.subtotal = subtotal;
        this.total = total;
    }

    public List<CarritoItemEnriquecidoDTO> getItems() {
        return items;
    }

    public void setItems(List<CarritoItemEnriquecidoDTO> items) {
        this.items = items;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
