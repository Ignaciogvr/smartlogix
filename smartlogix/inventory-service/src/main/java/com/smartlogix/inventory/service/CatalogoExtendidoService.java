package com.smartlogix.inventory.service;

import com.smartlogix.inventory.model.Subcategoria;
import com.smartlogix.inventory.model.AtributoProducto;
import com.smartlogix.inventory.model.VarianteProducto;

import java.util.List;

public interface CatalogoExtendidoService {
    
    // Subcategoria
    List<Subcategoria> getAllSubcategorias();
    List<Subcategoria> getSubcategoriasByCategoria(Long categoriaId);
    Subcategoria getSubcategoriaById(Long id);
    Subcategoria createSubcategoria(Subcategoria subcategoria);
    Subcategoria updateSubcategoria(Long id, Subcategoria subcategoria);
    void deleteSubcategoria(Long id);

    // AtributoProducto
    List<AtributoProducto> getAtributosByProducto(Long productoId);
    AtributoProducto getAtributoById(Long id);
    AtributoProducto createAtributo(AtributoProducto atributo);
    AtributoProducto updateAtributo(Long id, AtributoProducto atributo);
    void deleteAtributo(Long id);

    // VarianteProducto
    List<VarianteProducto> getVariantesByProducto(Long productoId);
    VarianteProducto getVarianteById(Long id);
    VarianteProducto getVarianteBySku(String sku);
    VarianteProducto createVariante(VarianteProducto variante);
    VarianteProducto updateVariante(Long id, VarianteProducto variante);
    void deleteVariante(Long id);
}
