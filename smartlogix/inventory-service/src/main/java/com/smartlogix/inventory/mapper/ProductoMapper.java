package com.smartlogix.inventory.mapper;

import com.smartlogix.inventory.dto.ProductoCreateRequest;
import com.smartlogix.inventory.dto.ProductoResponse;
import com.smartlogix.inventory.model.Producto;
import com.smartlogix.inventory.model.ProductoImagen;
import java.util.List;

public class ProductoMapper {

    public static Producto toEntity(ProductoCreateRequest req) {
        Producto producto = new Producto(
                req.getNombre(),
                req.getDescripcion(),
                req.getPrecio(),
                req.getStock(),
                req.getCategoria(),
                null
        );
        
        if (req.getImagenes() != null && !req.getImagenes().isEmpty()) {
            List<ProductoImagen> productImages = new java.util.ArrayList<>();
            int orden = 0;
            for (String url : req.getImagenes()) {
                ProductoImagen img = new ProductoImagen(url, false, orden);
                img.setProducto(producto);
                productImages.add(img);
                orden++;
            }
            
            if (!productImages.isEmpty()) {
                productImages.get(0).setEsPrincipal(true);
            }
            
            producto.setImagenes(productImages);
        }
        
        return producto;
    }

    public static ProductoResponse toResponse(Producto p) {
        String imagenPrincipal = null;
        List<String> imageUrls = null;
        
        if (p.getImagenes() != null && !p.getImagenes().isEmpty()) {
            imageUrls = p.getImagenes().stream()
                    .map(ProductoImagen::getUrl)
                    .toList();
                    
            imagenPrincipal = p.getImagenes().stream()
                    .filter(img -> Boolean.TRUE.equals(img.getEsPrincipal()))
                    .map(ProductoImagen::getUrl)
                    .findFirst()
                    .orElse(p.getImagenes().get(0).getUrl());
        }

        return new ProductoResponse(
                p.getId(),
                p.getNombre(),
                p.getDescripcion(),
                p.getPrecio(),
                p.getStock(),
                p.getCategoria(),
                imagenPrincipal,
                imageUrls,
                p.getRatingPromedio(),
                p.getCantidadVendidos(),
                p.getDestacado(),
                p.getOferta(),
                p.getNuevo(),
                p.getDescripcionCorta(),
                p.getPrecioAnterior(),
                p.getDescuentoPorcentaje(),
                p.getMarca(),
                p.getModelo(),
                p.getFabricante(),
                p.getSku(),
                p.getGarantia(),
                p.getPeso(),
                p.getDimensiones(),
                p.getMaterial(),
                p.getColor(),
                p.getPaisFabricacion(),
                p.getTotalRatings()
        );
    }
}