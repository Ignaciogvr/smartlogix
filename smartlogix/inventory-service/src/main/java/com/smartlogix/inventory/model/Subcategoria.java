package com.smartlogix.inventory.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subcategorias_producto", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"categoria_id", "nombre_subcategoria"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subcategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(name = "nombre_subcategoria", nullable = false, length = 100)
    private String nombreSubcategoria;

    @Column(length = 500)
    private String descripcion;

    @Column(length = 100)
    private String slug;

    @Column(name = "orden_visualizacion")
    private Integer ordenVisualizacion;

    private Boolean activa;
}
