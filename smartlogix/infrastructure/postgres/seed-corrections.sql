-- ====================================================================
-- CORRECCIONES Y DATOS ADICIONALES
-- ====================================================================

\c inventory_db

-- Insertar imágenes para productos (usando imagen_url en vez de url)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT p.id, 'https://images.unsplash.com/photo-1593642632823-8f785ba67e45', true, 1
FROM productos p WHERE p.nombre = 'Laptop Dell XPS 15' AND NOT EXISTS (
    SELECT 1 FROM producto_imagenes WHERE producto_id = p.id
)
UNION ALL
SELECT p.id, 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8', true, 1
FROM productos p WHERE p.nombre = 'MacBook Pro 14"' AND NOT EXISTS (
    SELECT 1 FROM producto_imagenes WHERE producto_id = p.id
)
UNION ALL
SELECT p.id, 'https://images.unsplash.com/photo-1588872657578-7efd1f1555ed', true, 1
FROM productos p WHERE p.nombre = 'Lenovo ThinkPad X1' AND NOT EXISTS (
    SELECT 1 FROM producto_imagenes WHERE producto_id = p.id
);

-- Insertar imágenes para los demás productos
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT p.id, 
       CASE 
           WHEN p.categoria = 'Monitores' THEN 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf'
           WHEN p.categoria = 'Periféricos' AND p.nombre LIKE '%Teclado%' THEN 'https://images.unsplash.com/photo-1595225476474-87563907a212'
           WHEN p.categoria = 'Periféricos' AND p.nombre LIKE '%Ratón%' THEN 'https://images.unsplash.com/photo-1527814050087-3793815479db'
           WHEN p.categoria = 'Periféricos' AND p.nombre LIKE '%Webcam%' THEN 'https://images.unsplash.com/photo-1570046057387-d63958dc7bd5'
           WHEN p.categoria = 'Audio' THEN 'https://images.unsplash.com/photo-1546435770-a3e426bf472b'
           WHEN p.categoria = 'Tablets' THEN 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0'
           WHEN p.categoria = 'Almacenamiento' THEN 'https://images.unsplash.com/photo-1597872200969-2b65d56bd16b'
           ELSE 'https://images.unsplash.com/photo-1593642632823-8f785ba67e45'
       END, 
       true, 1
FROM productos p
WHERE NOT EXISTS (SELECT 1 FROM producto_imagenes WHERE producto_id = p.id);

-- Insertar banners (usando ruta_destino en vez de link_url)
INSERT INTO banners (titulo, descripcion, imagen_url, ruta_destino, activo, orden)
VALUES
('Ofertas de Verano', 'Hasta 30% de descuento en laptops y monitores seleccionados', 
 'https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da', '/catalogo?categoria=Laptops', true, 1),
('Nuevo: MacBook Pro M2', 'Descubre la potencia del chip M2 Pro en el nuevo MacBook Pro', 
 'https://images.unsplash.com/photo-1517694712202-14dd9538aa97', '/producto/2', true, 2),
('Gaming Setup Completo', 'Arma tu estación gaming perfecta con nuestros periféricos', 
 'https://images.unsplash.com/photo-1593305841991-05c297ba4575', '/catalogo?categoria=Periféricos', true, 3),
('Almacenamiento Ultra Rápido', 'SSDs NVMe con hasta 25% de descuento', 
 'https://images.unsplash.com/photo-1597872200969-2b65d56bd16b', '/catalogo?categoria=Almacenamiento', true, 4);

-- Insertar comentarios (verificando nombre de columna)
INSERT INTO producto_comentarios (producto_id, usuario_id, calificacion, comentario)
SELECT p.id, 'auth0|user1', 5, 'Excelente producto, superó mis expectativas'
FROM productos p WHERE p.nombre = 'Laptop Dell XPS 15' LIMIT 1
UNION ALL
SELECT p.id, 'auth0|user2', 4, 'Buena calidad pero un poco cara'
FROM productos p WHERE p.nombre = 'Laptop Dell XPS 15' LIMIT 1
UNION ALL
SELECT p.id, 'auth0|user3', 5, 'La mejor inversión que he hecho'
FROM productos p WHERE p.nombre = 'MacBook Pro 14"' LIMIT 1
UNION ALL
SELECT p.id, 'auth0|user1', 5, 'Perfecto para mi trabajo, muy productivo'
FROM productos p WHERE p.nombre = 'Monitor LG UltraWide 34"' LIMIT 1
UNION ALL
SELECT p.id, 'auth0|user2', 5, 'Increíble comodidad para largas jornadas'
FROM productos p WHERE p.nombre = 'Logitech MX Master 3' LIMIT 1
UNION ALL
SELECT p.id, 'auth0|user3', 5, 'Cancelación de ruido impresionante'
FROM productos p WHERE p.nombre = 'Sony WH-1000XM5' LIMIT 1
UNION ALL
SELECT p.id, 'auth0|user1', 5, 'Perfecta para diseño y entretenimiento'
FROM productos p WHERE p.nombre = 'iPad Air M1' LIMIT 1;


-- ====================================================================
-- PEDIDOS_DB - Correcciones
-- ====================================================================

\c pedidos_db

-- Los pedidos y detalles ya están insertados, solo agregar relaciones restantes

-- ====================================================================
-- ENVIO_DB - Correcciones historial
-- ====================================================================

\c envio_db

-- Revisar estructura de historial_envios para ver columnas correctas
-- Insertar historial sin columna ubicacion si no existe
INSERT INTO historial_envios (envio_id, estado, descripcion, fecha_evento)
SELECT 1, 'PENDIENTE', 'Centro de distribución - Envío creado y registrado', CURRENT_TIMESTAMP - INTERVAL '26 days'
WHERE NOT EXISTS (SELECT 1 FROM historial_envios WHERE envio_id = 1 AND estado = 'PENDIENTE')
UNION ALL
SELECT 1, 'PREPARANDO', 'Centro de distribución - Paquete siendo preparado', CURRENT_TIMESTAMP - INTERVAL '26 days'
WHERE NOT EXISTS (SELECT 1 FROM historial_envios WHERE envio_id = 1 AND estado = 'PREPARANDO')
UNION ALL
SELECT 1, 'EN_RUTA', 'En camino - Paquete despachado', CURRENT_TIMESTAMP - INTERVAL '25 days'
WHERE NOT EXISTS (SELECT 1 FROM historial_envios WHERE envio_id = 1 AND estado = 'EN_RUTA' AND fecha_evento::date = (CURRENT_TIMESTAMP - INTERVAL '25 days')::date)
UNION ALL
SELECT 1, 'ENTREGADO', 'Av. Libertador 1234 - Entregado exitosamente', CURRENT_TIMESTAMP - INTERVAL '23 days'
WHERE NOT EXISTS (SELECT 1 FROM historial_envios WHERE envio_id = 1 AND estado = 'ENTREGADO')
UNION ALL
SELECT 2, 'PENDIENTE', 'Centro de distribución - Envío creado y registrado', CURRENT_TIMESTAMP - INTERVAL '22 days'
WHERE NOT EXISTS (SELECT 1 FROM historial_envios WHERE envio_id = 2 AND estado = 'PENDIENTE')
UNION ALL
SELECT 2, 'PREPARANDO', 'Centro de distribución - Paquete siendo preparado', CURRENT_TIMESTAMP - INTERVAL '22 days'
WHERE NOT EXISTS (SELECT 1 FROM historial_envios WHERE envio_id = 2 AND estado = 'PREPARANDO')
UNION ALL
SELECT 2, 'EN_RUTA', 'En camino - Paquete despachado - Envío Express', CURRENT_TIMESTAMP - INTERVAL '21 days'
WHERE NOT EXISTS (SELECT 1 FROM historial_envios WHERE envio_id = 2 AND estado = 'EN_RUTA')
UNION ALL
SELECT 2, 'ENTREGADO', 'Calle Merced 567 - Entregado exitosamente', CURRENT_TIMESTAMP - INTERVAL '20 days'
WHERE NOT EXISTS (SELECT 1 FROM historial_envios WHERE envio_id = 2 AND estado = 'ENTREGADO')
UNION ALL
SELECT 3, 'PENDIENTE', 'Centro de distribución - Envío creado y registrado', CURRENT_TIMESTAMP - INTERVAL '3 days'
WHERE NOT EXISTS (SELECT 1 FROM historial_envios WHERE envio_id = 3 AND estado = 'PENDIENTE')
UNION ALL
SELECT 3, 'PREPARANDO', 'Centro de distribución - Paquete siendo preparado', CURRENT_TIMESTAMP - INTERVAL '3 days'
WHERE NOT EXISTS (SELECT 1 FROM historial_envios WHERE envio_id = 3 AND estado = 'PREPARANDO')
UNION ALL
SELECT 3, 'EN_RUTA', 'En camino - Paquete despachado', CURRENT_TIMESTAMP - INTERVAL '2 days'
WHERE NOT EXISTS (SELECT 1 FROM historial_envios WHERE envio_id = 3 AND estado = 'EN_RUTA' AND fecha_evento::date = (CURRENT_TIMESTAMP - INTERVAL '2 days')::date)
UNION ALL
SELECT 3, 'EN_RUTA', 'Vitacura - Llegó a punto de entrega', CURRENT_TIMESTAMP - INTERVAL '6 hours'
WHERE NOT EXISTS (SELECT 1 FROM historial_envios WHERE envio_id = 3 AND estado = 'EN_RUTA' AND descripcion LIKE '%Vitacura%')
UNION ALL
SELECT 4, 'PENDIENTE', 'Centro de distribución - Envío creado y registrado', CURRENT_TIMESTAMP - INTERVAL '1 day'
WHERE NOT EXISTS (SELECT 1 FROM historial_envios WHERE envio_id = 4 AND estado = 'PENDIENTE')
UNION ALL
SELECT 4, 'PREPARANDO', 'Centro de distribución - Paquete siendo preparado', CURRENT_TIMESTAMP - INTERVAL '1 day'
WHERE NOT EXISTS (SELECT 1 FROM historial_envios WHERE envio_id = 4 AND estado = 'PREPARANDO')
UNION ALL
SELECT 8, 'PENDIENTE', 'Centro de distribución - Envío creado, esperando procesamiento', CURRENT_TIMESTAMP - INTERVAL '1 hour'
WHERE NOT EXISTS (SELECT 1 FROM historial_envios WHERE envio_id = 8);


-- ====================================================================
-- USUARIOS_DB - Correcciones (sin campo telefono si no existe)
-- ====================================================================

\c usuarios_db

-- Insertar usuarios sin campo telefono
INSERT INTO usuarios (id, email, nombre, activo, fecha_creacion, fecha_actualizacion)
VALUES
('auth0|user1', 'juan.perez@example.com', 'Juan Pérez', true, 
 CURRENT_TIMESTAMP - INTERVAL '60 days', CURRENT_TIMESTAMP),
('auth0|user2', 'maria.garcia@example.com', 'María García', true, 
 CURRENT_TIMESTAMP - INTERVAL '50 days', CURRENT_TIMESTAMP),
('auth0|user3', 'carlos.lopez@example.com', 'Carlos López', true, 
 CURRENT_TIMESTAMP - INTERVAL '45 days', CURRENT_TIMESTAMP),
('auth0|seller1', 'vendedor1@smartlogix.com', 'Roberto Vendedor', true, 
 CURRENT_TIMESTAMP - INTERVAL '180 days', CURRENT_TIMESTAMP),
('auth0|seller2', 'vendedor2@smartlogix.com', 'Patricia Comercial', true, 
 CURRENT_TIMESTAMP - INTERVAL '150 days', CURRENT_TIMESTAMP),
('auth0|chofer1', 'chofer1@smartlogix.com', 'Diego Conductor', true, 
 CURRENT_TIMESTAMP - INTERVAL '120 days', CURRENT_TIMESTAMP),
('auth0|chofer2', 'chofer2@smartlogix.com', 'Andrea Transportista', true, 
 CURRENT_TIMESTAMP - INTERVAL '100 days', CURRENT_TIMESTAMP),
('auth0|admin1', 'admin@smartlogix.com', 'Administrador Principal', true, 
 CURRENT_TIMESTAMP - INTERVAL '365 days', CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- ====================================================================
-- FIN DEL SCRIPT DE CORRECCIONES
-- ====================================================================
