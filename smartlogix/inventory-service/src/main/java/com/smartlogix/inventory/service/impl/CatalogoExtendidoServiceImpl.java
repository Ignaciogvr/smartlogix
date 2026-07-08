package com.smartlogix.inventory.service.impl;

import com.smartlogix.inventory.model.Subcategoria;
import com.smartlogix.inventory.model.AtributoProducto;
import com.smartlogix.inventory.model.VarianteProducto;
import com.smartlogix.inventory.repository.SubcategoriaRepository;
import com.smartlogix.inventory.repository.AtributoProductoRepository;
import com.smartlogix.inventory.repository.VarianteProductoRepository;
import com.smartlogix.inventory.service.CatalogoExtendidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogoExtendidoServiceImpl implements CatalogoExtendidoService {

    private final SubcategoriaRepository subcategoriaRepository;
    private final AtributoProductoRepository atributoProductoRepository;
    private final VarianteProductoRepository varianteProductoRepository;

    @Override
    public List<Subcategoria> getAllSubcategorias() {
        return subcategoriaRepository.findAll();
    }

    @Override
    public List<Subcategoria> getSubcategoriasByCategoria(Long categoriaId) {
        return subcategoriaRepository.findByCategoriaId(categoriaId);
    }

    @Override
    public Subcategoria getSubcategoriaById(Long id) {
        return subcategoriaRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Subcategoria createSubcategoria(Subcategoria subcategoria) {
        return subcategoriaRepository.save(subcategoria);
    }

    @Override
    @Transactional
    public Subcategoria updateSubcategoria(Long id, Subcategoria subcategoria) {
        if (subcategoriaRepository.existsById(id)) {
            subcategoria.setId(id);
            return subcategoriaRepository.save(subcategoria);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteSubcategoria(Long id) {
        subcategoriaRepository.deleteById(id);
    }

    @Override
    public List<AtributoProducto> getAtributosByProducto(Long productoId) {
        return atributoProductoRepository.findByProductoId(productoId);
    }

    @Override
    public AtributoProducto getAtributoById(Long id) {
        return atributoProductoRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public AtributoProducto createAtributo(AtributoProducto atributo) {
        return atributoProductoRepository.save(atributo);
    }

    @Override
    @Transactional
    public AtributoProducto updateAtributo(Long id, AtributoProducto atributo) {
        if (atributoProductoRepository.existsById(id)) {
            atributo.setId(id);
            return atributoProductoRepository.save(atributo);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteAtributo(Long id) {
        atributoProductoRepository.deleteById(id);
    }

    @Override
    public List<VarianteProducto> getVariantesByProducto(Long productoId) {
        return varianteProductoRepository.findByProductoId(productoId);
    }

    @Override
    public VarianteProducto getVarianteById(Long id) {
        return varianteProductoRepository.findById(id).orElse(null);
    }

    @Override
    public VarianteProducto getVarianteBySku(String sku) {
        return varianteProductoRepository.findByCodigoSku(sku).orElse(null);
    }

    @Override
    @Transactional
    public VarianteProducto createVariante(VarianteProducto variante) {
        return varianteProductoRepository.save(variante);
    }

    @Override
    @Transactional
    public VarianteProducto updateVariante(Long id, VarianteProducto variante) {
        if (varianteProductoRepository.existsById(id)) {
            variante.setId(id);
            return varianteProductoRepository.save(variante);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteVariante(Long id) {
        varianteProductoRepository.deleteById(id);
    }
}
