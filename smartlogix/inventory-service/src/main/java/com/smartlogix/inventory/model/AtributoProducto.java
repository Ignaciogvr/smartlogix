package com.smartlogix.inventory.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "atributos_producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtributoProducto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "nombre_atributo", nullable = false, length = 100)
    private String nombreAtributo;

    @Column(name = "valor_atributo", nullable = false, length = 100)
    private String valorAtributo;
}
