-- ============================================
-- SmartLogix - Inventory Service
-- Migration: V8__clean_duplicate_images.sql
-- Description: Clean duplicate images and fix associations
-- ============================================

-- Eliminar duplicados de producto_imagenes (mismo producto_id + misma url)
DELETE FROM producto_imagenes 
WHERE id NOT IN (
    SELECT MIN(id) 
    FROM producto_imagenes 
    GROUP BY producto_id, imagen_url
);

-- Actualizar campo orden para todas las imágenes
-- Esto asegura que cada imagen tenga un orden secuencial por producto
WITH ordered_images AS (
    SELECT 
        id,
        producto_id,
        imagen_url,
        ROW_NUMBER() OVER (PARTITION BY producto_id ORDER BY id) - 1 as new_orden
    FROM producto_imagenes
)
UPDATE producto_imagenes pi
SET orden = oi.new_orden
FROM ordered_images oi
WHERE pi.id = oi.id;

-- Asegurar que la primera imagen de cada producto tenga es_principal = true
UPDATE producto_imagenes pi
SET es_principal = true
WHERE id = (
    SELECT MIN(id)
    FROM producto_imagenes
    WHERE producto_id = pi.producto_id
);

-- Asegurar que las demás imágenes tengan es_principal = false
UPDATE producto_imagenes pi
SET es_principal = false
WHERE id NOT IN (
    SELECT MIN(id)
    FROM producto_imagenes
    GROUP BY producto_id
);

-- Verificar integridad: eliminar imágenes huérfanas (sin producto válido)
DELETE FROM producto_imagenes 
WHERE producto_id NOT IN (SELECT id FROM productos);

-- Verificar integridad: eliminar productos sin imágenes (opcional, descomentar si se desea)
-- DELETE FROM productos 
-- WHERE id NOT IN (SELECT DISTINCT producto_id FROM producto_imagenes);
