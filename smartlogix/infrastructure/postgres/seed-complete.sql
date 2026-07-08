-- ====================================================================
-- SCRIPT COMPLETO DE DATOS - SMARTLOGIX
-- ====================================================================

-- ====================================================================
-- INVENTORY_DB
-- ====================================================================
\c inventory_db

-- Insertar imágenes para productos existentes
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT p.id, 'https://images.unsplash.com/photo-1593642632823-8f785ba67e45', true, 1
FROM productos p WHERE p.nombre LIKE '%Dell%' 
AND NOT EXISTS (SELECT 1 FROM producto_imagenes WHERE producto_id = p.id)
LIMIT 1;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT p.id, 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8', true, 1
FROM productos p WHERE p.nombre LIKE '%MacBook%' 
AND NOT EXISTS (SELECT 1 FROM producto_imagenes WHERE producto_id = p.id)
LIMIT 1;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT p.id, 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf', true, 1
FROM productos p WHERE p.nombre LIKE '%Monitor%' 
AND NOT EXISTS (SELECT 1 FROM producto_imagenes WHERE producto_id = p.id)
LIMIT 1;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT p.id, 'https://images.unsplash.com/photo-1595225476474-87563907a212', true, 1
FROM productos p WHERE p.nombre LIKE '%Teclado%' 
AND NOT EXISTS (SELECT 1 FROM producto_imagenes WHERE producto_id = p.id)
LIMIT 1;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT p.id, 'https://images.unsplash.com/photo-1527814050087-3793815479db', true, 1
FROM productos p WHERE p.nombre LIKE '%Logitech MX%' 
AND NOT EXISTS (SELECT 1 FROM producto_imagenes WHERE producto_id = p.id)
LIMIT 1;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT p.id, 'https://images.unsplash.com/photo-1546435770-a3e426bf472b', true, 1
FROM productos p WHERE p.categoria = 'Periféricos' AND p.nombre LIKE '%Audífonos%'
AND NOT EXISTS (SELECT 1 FROM producto_imagenes WHERE producto_id = p.id)
LIMIT 1;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT p.id, 'https://images.unsplash.com/photo-1570046057387-d63958dc7bd5', true, 1
FROM productos p WHERE p.nombre LIKE '%Webcam%'
AND NOT EXISTS (SELECT 1 FROM producto_imagenes WHERE producto_id = p.id)
LIMIT 1;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT p.id, 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0', true, 1
FROM productos p WHERE p.nombre LIKE '%iPad%' OR p.nombre LIKE '%Galaxy Tab%'
AND NOT EXISTS (SELECT 1 FROM producto_imagenes WHERE producto_id = p.id)
LIMIT 1;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT p.id, 'https://images.unsplash.com/photo-1597872200969-2b65d56bd16b', true, 1
FROM productos p WHERE p.categoria = 'Almacenamiento'
AND NOT EXISTS (SELECT 1 FROM producto_imagenes WHERE producto_id = p.id)
LIMIT 1;

-- Insertar banners
DELETE FROM banners;
INSERT INTO banners (titulo, descripcion, imagen_url, ruta_destino, activo, orden)
VALUES
('Ofertas de Verano', 'Hasta 30% de descuento en laptops y monitores', 
 'https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da', '/catalogo', true, 1),
('MacBook Pro M2', 'Potencia profesional con chip M2 Pro', 
 'https://images.unsplash.com/photo-1517694712202-14dd9538aa97', '/catalogo', true, 2),
('Gaming Setup', 'Todo para tu estación gaming perfecta', 
 'https://images.unsplash.com/photo-1593305841991-05c297ba4575', '/catalogo', true, 3),
('Almacenamiento Rápido', 'SSDs NVMe con descuento', 
 'https://images.unsplash.com/photo-1597872200969-2b65d56bd16b', '/catalogo', true, 4);

-- Insertar comentarios (usando created_at en vez de fecha_creacion)
INSERT INTO producto_comentarios (producto_id, usuario_id, calificacion, comentario, created_at)
SELECT p.id, 'auth0|user1', 5, 'Excelente producto, superó expectativas', CURRENT_TIMESTAMP - INTERVAL '10 days'
FROM productos p WHERE p.nombre LIKE '%Dell%' LIMIT 1;

INSERT INTO producto_comentarios (producto_id, usuario_id, calificacion, comentario, created_at)
SELECT p.id, 'auth0|user2', 4, 'Buena calidad precio', CURRENT_TIMESTAMP - INTERVAL '8 days'
FROM productos p WHERE p.nombre LIKE '%Dell%' LIMIT 1;

INSERT INTO producto_comentarios (producto_id, usuario_id, calificacion, comentario, created_at)
SELECT p.id, 'auth0|user3', 5, 'La mejor inversión que he hecho', CURRENT_TIMESTAMP - INTERVAL '15 days'
FROM productos p WHERE p.nombre LIKE '%MacBook%' LIMIT 1;

INSERT INTO producto_comentarios (producto_id, usuario_id, calificacion, comentario, created_at)
SELECT p.id, 'auth0|user1', 5, 'Perfecto para trabajo desde casa', CURRENT_TIMESTAMP - INTERVAL '5 days'
FROM productos p WHERE p.nombre LIKE '%Monitor LG%' LIMIT 1;

INSERT INTO producto_comentarios (producto_id, usuario_id, calificacion, comentario, created_at)
SELECT p.id, 'auth0|user2', 5, 'Muy cómodo, lo recomiendo', CURRENT_TIMESTAMP - INTERVAL '3 days'
FROM productos p WHERE p.nombre LIKE '%Logitech MX%' LIMIT 1;

-- ====================================================================
-- ENVIO_DB
-- ====================================================================
\c envio_db

-- Insertar historial de envíos (usando columna 'fecha' y verificando que existan los envíos)
DELETE FROM historial_envios WHERE envio_id IN (1,2,3,4,5,6,7);

INSERT INTO historial_envios (envio_id, estado, descripcion, fecha)
SELECT 1, 'PENDIENTE', 'Centro distribución - Envío creado', CURRENT_TIMESTAMP - INTERVAL '26 days'
WHERE EXISTS (SELECT 1 FROM envios WHERE id = 1)
UNION ALL
SELECT 1, 'PREPARANDO', 'Centro distribución - Preparando paquete', CURRENT_TIMESTAMP - INTERVAL '26 days'
WHERE EXISTS (SELECT 1 FROM envios WHERE id = 1)
UNION ALL
SELECT 1, 'EN_RUTA', 'En camino - Despachado', CURRENT_TIMESTAMP - INTERVAL '25 days'
WHERE EXISTS (SELECT 1 FROM envios WHERE id = 1)
UNION ALL
SELECT 1, 'ENTREGADO', 'Av. Libertador 1234 - Entregado', CURRENT_TIMESTAMP - INTERVAL '23 days'
WHERE EXISTS (SELECT 1 FROM envios WHERE id = 1)
UNION ALL
SELECT 2, 'PENDIENTE', 'Centro distribución - Envío creado', CURRENT_TIMESTAMP - INTERVAL '22 days'
WHERE EXISTS (SELECT 1 FROM envios WHERE id = 2)
UNION ALL
SELECT 2, 'PREPARANDO', 'Centro distribución - Preparando', CURRENT_TIMESTAMP - INTERVAL '22 days'
WHERE EXISTS (SELECT 1 FROM envios WHERE id = 2)
UNION ALL
SELECT 2, 'EN_RUTA', 'Express - Despachado', CURRENT_TIMESTAMP - INTERVAL '21 days'
WHERE EXISTS (SELECT 1 FROM envios WHERE id = 2)
UNION ALL
SELECT 2, 'ENTREGADO', 'Calle Merced 567 - Entregado', CURRENT_TIMESTAMP - INTERVAL '20 days'
WHERE EXISTS (SELECT 1 FROM envios WHERE id = 2)
UNION ALL
SELECT 3, 'PENDIENTE', 'Centro distribución - Envío creado', CURRENT_TIMESTAMP - INTERVAL '3 days'
WHERE EXISTS (SELECT 1 FROM envios WHERE id = 3)
UNION ALL
SELECT 3, 'PREPARANDO', 'Centro distribución - Preparando', CURRENT_TIMESTAMP - INTERVAL '3 days'
WHERE EXISTS (SELECT 1 FROM envios WHERE id = 3)
UNION ALL
SELECT 3, 'EN_RUTA', 'En camino - Despachado', CURRENT_TIMESTAMP - INTERVAL '2 days'
WHERE EXISTS (SELECT 1 FROM envios WHERE id = 3)
UNION ALL
SELECT 3, 'EN_RUTA', 'Vitacura - Punto de entrega', CURRENT_TIMESTAMP - INTERVAL '6 hours'
WHERE EXISTS (SELECT 1 FROM envios WHERE id = 3)
UNION ALL
SELECT 4, 'PENDIENTE', 'Centro distribución - Envío creado', CURRENT_TIMESTAMP - INTERVAL '1 day'
WHERE EXISTS (SELECT 1 FROM envios WHERE id = 4)
UNION ALL
SELECT 4, 'PREPARANDO', 'Centro distribución - Preparando', CURRENT_TIMESTAMP - INTERVAL '1 day'
WHERE EXISTS (SELECT 1 FROM envios WHERE id = 4)
UNION ALL
SELECT 5, 'PENDIENTE', 'Centro distribución - Esperando procesamiento', CURRENT_TIMESTAMP - INTERVAL '2 hours'
WHERE EXISTS (SELECT 1 FROM envios WHERE id = 5)
UNION ALL
SELECT 6, 'PENDIENTE', 'Centro distribución - Esperando procesamiento', CURRENT_TIMESTAMP - INTERVAL '1 hour'
WHERE EXISTS (SELECT 1 FROM envios WHERE id = 6)
UNION ALL
SELECT 7, 'CANCELADO', 'Pedido cancelado por el cliente', CURRENT_TIMESTAMP - INTERVAL '5 days'
WHERE EXISTS (SELECT 1 FROM envios WHERE id = 7);

-- ====================================================================
-- USUARIOS_DB
-- ====================================================================
\c usuarios_db

-- Insertar usuarios usando auth0_id
INSERT INTO usuarios (auth0_id, email, nombre, estado, rol, fecha_creacion, fecha_actualizacion)
VALUES
('auth0|user1', 'juan.perez@example.com', 'Juan Pérez', 'ACTIVO', 'CLIENTE',
 CURRENT_TIMESTAMP - INTERVAL '60 days', CURRENT_TIMESTAMP),
('auth0|user2', 'maria.garcia@example.com', 'María García', 'ACTIVO', 'CLIENTE',
 CURRENT_TIMESTAMP - INTERVAL '50 days', CURRENT_TIMESTAMP),
('auth0|user3', 'carlos.lopez@example.com', 'Carlos López', 'ACTIVO', 'CLIENTE',
 CURRENT_TIMESTAMP - INTERVAL '45 days', CURRENT_TIMESTAMP),
('auth0|seller1', 'vendedor1@smartlogix.com', 'Roberto Vendedor', 'ACTIVO', 'VENDEDOR',
 CURRENT_TIMESTAMP - INTERVAL '180 days', CURRENT_TIMESTAMP),
('auth0|seller2', 'vendedor2@smartlogix.com', 'Patricia Comercial', 'ACTIVO', 'VENDEDOR',
 CURRENT_TIMESTAMP - INTERVAL '150 days', CURRENT_TIMESTAMP),
('auth0|chofer1', 'chofer1@smartlogix.com', 'Diego Conductor', 'ACTIVO', 'CHOFER',
 CURRENT_TIMESTAMP - INTERVAL '120 days', CURRENT_TIMESTAMP),
('auth0|chofer2', 'chofer2@smartlogix.com', 'Andrea Transportista', 'ACTIVO', 'CHOFER',
 CURRENT_TIMESTAMP - INTERVAL '100 days', CURRENT_TIMESTAMP),
('auth0|admin1', 'admin@smartlogix.com', 'Admin Principal', 'ACTIVO', 'ADMIN',
 CURRENT_TIMESTAMP - INTERVAL '365 days', CURRENT_TIMESTAMP)
ON CONFLICT (auth0_id) DO NOTHING;

-- ====================================================================
-- FIN
-- ====================================================================
