package com.smartlogix.inventory.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "variantes_producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VarianteProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "codigo_sku", unique = true, nullable = false, length = 100)
    private String codigoSku;

    @Column(name = "nombre_variante", length = 255)
    private String nombreVariante;

    @Column(name = "precio_variante")
    private Double precioVariante;

    @Column(name = "stock_variante")
    private Integer stockVariante;

    @Column(name = "atributos_json", length = 500)
    private String atributosJson;

    @Column(name = "imagen_variante_url", length = 500)
    private String imagenVarianteUrl;

    @Column(length = 50)
    private String estado;
}
