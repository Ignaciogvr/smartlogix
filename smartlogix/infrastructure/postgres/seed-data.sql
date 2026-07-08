-- ====================================================================
-- SCRIPT DE DATOS DE PRUEBA PARA SMARTLOGIX
-- ====================================================================
-- Este script inserta datos realistas en todas las bases de datos
-- para que el frontend se vea completo y profesional.
--
-- EJECUTAR: docker exec -i postgres psql -U postgres < seed-data.sql
-- ====================================================================

-- ====================================================================
-- 1. INVENTORY_DB - Productos
-- ====================================================================

\c inventory_db

-- Limpiar datos existentes
TRUNCATE TABLE reviews CASCADE;
TRUNCATE TABLE productos CASCADE;
TRUNCATE TABLE banners CASCADE;

-- Insertar productos realistas
INSERT INTO productos (id, nombre, descripcion, categoria, precio, stock, imagen_principal, imagenes_adicionales, 
                      marca, modelo, destacado, oferta, nuevo, descuento, rating, total_ratings, vendidos, 
                      garantia, peso, dimensiones, activo, vendedor_id, created_at, updated_at)
VALUES
-- Laptops
(1, 'Laptop Dell XPS 15', 'Potente laptop para profesionales con pantalla 4K', 'Laptops', 1299.99, 25, 
 'https://images.unsplash.com/photo-1593642632823-8f785ba67e45', 
 '["https://images.unsplash.com/photo-1496181133206-80ce9b88a853", "https://images.unsplash.com/photo-1517336714731-489689fd1ca8"]',
 'Dell', 'XPS 15 9520', true, false, false, 0, 4.7, 142, 89, '2 años', 2.1, '35.7 x 23.5 x 1.8 cm', true, 
 'auth0|seller1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
 
(2, 'MacBook Pro 14"', 'MacBook Pro con chip M2 Pro', 'Laptops', 1999.99, 15,
 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8',
 '["https://images.unsplash.com/photo-1611186871348-b1ce696e52c9", "https://images.unsplash.com/photo-1517694712202-14dd9538aa97"]',
 'Apple', 'MacBook Pro 14 M2', true, true, false, 15, 4.9, 234, 156, '1 año', 1.6, '31.26 x 22.12 x 1.55 cm', true,
 'auth0|seller1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);


-- Monitores
(3, 'Monitor LG UltraWide 34"', 'Monitor ultrawide para productividad extrema', 'Monitores', 599.99, 40,
 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf',
 '["https://images.unsplash.com/photo-1585792180666-f7347c490ee2", "https://images.unsplash.com/photo-1593359677879-a4bb92f829d1"]',
 'LG', '34WN80C-B', true, false, true, 0, 4.6, 98, 67, '3 años', 6.8, '81.5 x 56.2 x 26.7 cm', true,
 'auth0|seller2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(4, 'Samsung Odyssey G7 27"', 'Monitor gaming curvo 240Hz', 'Monitores', 799.99, 18,
 'https://images.unsplash.com/photo-1593305841991-05c297ba4575',
 '["https://images.unsplash.com/photo-1585792180666-f7347c490ee2"]',
 'Samsung', 'Odyssey G7', false, true, false, 20, 4.8, 187, 134, '1 año', 8.2, '61.4 x 46.9 x 29.1 cm', true,
 'auth0|seller2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Teclados
(5, 'Teclado Mecánico Corsair K95', 'Teclado gaming mecánico RGB', 'Periféricos', 179.99, 60,
 'https://images.unsplash.com/photo-1595225476474-87563907a212',
 '["https://images.unsplash.com/photo-1587829741301-dc798b83add3", "https://images.unsplash.com/photo-1511467687858-23d96c32e4ae"]',
 'Corsair', 'K95 RGB Platinum', false, false, true, 0, 4.5, 203, 178, '2 años', 1.3, '46.5 x 17 x 3.9 cm', true,
 'auth0|seller1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(6, 'Logitech MX Keys', 'Teclado inalámbrico para profesionales', 'Periféricos', 99.99, 85,
 'https://images.unsplash.com/photo-1618384887929-16ec33fab9ef',
 '["https://images.unsplash.com/photo-1587829741301-dc798b83add3"]',
 'Logitech', 'MX Keys', true, false, false, 0, 4.7, 412, 389, '1 año', 0.8, '43.0 x 13.1 x 2.0 cm', true,
 'auth0|seller1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Ratones
(7, 'Logitech MX Master 3', 'Ratón inalámbrico ergonómico', 'Periféricos', 99.99, 120,
 'https://images.unsplash.com/photo-1527814050087-3793815479db',
 '["https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7"]',
 'Logitech', 'MX Master 3', true, true, false, 10, 4.8, 567, 523, '1 año', 0.14, '12.4 x 8.4 x 5.1 cm', true,
 'auth0|seller2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(8, 'Razer DeathAdder V2', 'Ratón gaming de alta precisión', 'Periféricos', 69.99, 95,
 'https://images.unsplash.com/photo-1563297007-0686b7003af7',
 '["https://images.unsplash.com/photo-1601944179066-29786cb9d32a"]',
 'Razer', 'DeathAdder V2', false, false, true, 0, 4.6, 289, 245, '2 años', 0.08, '12.7 x 6.1 x 4.3 cm', true,
 'auth0|seller2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Auriculares
(9, 'Sony WH-1000XM5', 'Auriculares con cancelación de ruido premium', 'Audio', 399.99, 45,
 'https://images.unsplash.com/photo-1546435770-a3e426bf472b',
 '["https://images.unsplash.com/photo-1484704849700-f032a568e944", "https://images.unsplash.com/photo-1545127398-14699f92334b"]',
 'Sony', 'WH-1000XM5', true, false, true, 0, 4.9, 723, 689, '1 año', 0.25, '25.4 x 21.4 x 8.2 cm', true,
 'auth0|seller1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(10, 'HyperX Cloud II', 'Auriculares gaming con sonido 7.1', 'Audio', 99.99, 70,
 'https://images.unsplash.com/photo-1577174881658-0f30157d96c4',
 '["https://images.unsplash.com/photo-1545127398-14699f92334b"]',
 'HyperX', 'Cloud II', false, true, false, 15, 4.7, 445, 412, '2 años', 0.32, '17.2 x 9.3 x 18.4 cm', true,
 'auth0|seller2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Webcams
(11, 'Logitech C920 HD Pro', 'Webcam Full HD 1080p', 'Periféricos', 79.99, 110,
 'https://images.unsplash.com/photo-1570046057387-d63958dc7bd5',
 '["https://images.unsplash.com/photo-1587829741301-dc798b83add3"]',
 'Logitech', 'C920', false, false, false, 0, 4.5, 892, 834, '2 años', 0.16, '9.4 x 4.3 x 7.1 cm', true,
 'auth0|seller1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(12, 'Elgato Facecam', 'Webcam profesional para streaming', 'Periféricos', 169.99, 32,
 'https://images.unsplash.com/photo-1517694712202-14dd9538aa97',
 '["https://images.unsplash.com/photo-1585792180666-f7347c490ee2"]',
 'Elgato', 'Facecam', true, false, true, 0, 4.6, 178, 145, '2 años', 0.15, '5.8 x 5.8 x 6.6 cm', true,
 'auth0|seller2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Tablets
(13, 'iPad Air M1', 'Tablet potente con chip M1', 'Tablets', 599.99, 35,
 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0',
 '["https://images.unsplash.com/photo-1585790050230-5dd28404f28a", "https://images.unsplash.com/photo-1561154464-82e9adf32764"]',
 'Apple', 'iPad Air 5', true, false, false, 0, 4.8, 356, 312, '1 año', 0.46, '24.76 x 17.85 x 0.61 cm', true,
 'auth0|seller1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(14, 'Samsung Galaxy Tab S8', 'Tablet Android premium', 'Tablets', 699.99, 28,
 'https://images.unsplash.com/photo-1561154464-82e9adf32764',
 '["https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0"]',
 'Samsung', 'Galaxy Tab S8', false, true, false, 10, 4.6, 234, 198, '1 año', 0.50, '25.3 x 16.5 x 0.64 cm', true,
 'auth0|seller2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Impresoras
(15, 'HP LaserJet Pro M404dn', 'Impresora láser monocromática', 'Impresoras', 279.99, 22,
 'https://images.unsplash.com/photo-1612815154858-60aa4c59eaa6',
 '["https://images.unsplash.com/photo-1612815154858-60aa4c59eaa6"]',
 'HP', 'LaserJet Pro M404dn', false, false, true, 0, 4.4, 167, 142, '1 año', 10.2, '37.4 x 35.9 x 31.8 cm', true,
 'auth0|seller1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Almacenamiento
(16, 'Samsung SSD 1TB 980 PRO', 'SSD NVMe ultra rápido', 'Almacenamiento', 139.99, 150,
 'https://images.unsplash.com/photo-1597872200969-2b65d56bd16b',
 '["https://images.unsplash.com/photo-1597872200969-2b65d56bd16b"]',
 'Samsung', '980 PRO', true, true, false, 25, 4.9, 1234, 1089, '5 años', 0.008, '8.0 x 2.2 x 0.23 cm', true,
 'auth0|seller1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

(17, 'WD My Passport 2TB', 'Disco duro externo portátil', 'Almacenamiento', 79.99, 200,
 'https://images.unsplash.com/photo-1597872200969-2b65d56bd16b',
 '["https://images.unsplash.com/photo-1597872200969-2b65d56bd16b"]',
 'Western Digital', 'My Passport', false, false, false, 0, 4.5, 789, 723, '2 años', 0.17, '10.7 x 7.5 x 1.9 cm', true,
 'auth0|seller2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Resetear secuencia
SELECT setval('productos_id_seq', (SELECT MAX(id) FROM productos));

-- Insertar banners
INSERT INTO banners (id, titulo, descripcion, imagen_url, link_url, activo, orden, fecha_inicio, fecha_fin, created_at, updated_at)
VALUES
(1, 'Ofertas de Verano', 'Hasta 30% de descuento en laptops y monitores', 
 'https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da', '/catalogo?categoria=Laptops', 
 true, 1, CURRENT_DATE, CURRENT_DATE + INTERVAL '30 days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
 
(2, 'Nuevo: MacBook Pro M2', 'Potencia profesional con el chip M2 Pro', 
 'https://images.unsplash.com/photo-1517694712202-14dd9538aa97', '/producto/2', 
 true, 2, CURRENT_DATE, CURRENT_DATE + INTERVAL '60 days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
 
(3, 'Gaming Setup Completo', 'Todo lo que necesitas para tu estación gaming', 
 'https://images.unsplash.com/photo-1593305841991-05c297ba4575', '/catalogo?categoria=Periféricos', 
 true, 3, CURRENT_DATE, CURRENT_DATE + INTERVAL '45 days', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

SELECT setval('banners_id_seq', (SELECT MAX(id) FROM banners));

-- Insertar reseñas/comentarios
INSERT INTO reviews (id, producto_id, usuario_id, rating, comentario, created_at)
VALUES
(1, 1, 'auth0|user1', 5, 'Excelente laptop, muy rápida y con gran pantalla', CURRENT_TIMESTAMP - INTERVAL '10 days'),
(2, 1, 'auth0|user2', 4, 'Buena calidad pero un poco cara', CURRENT_TIMESTAMP - INTERVAL '8 days'),
(3, 2, 'auth0|user3', 5, 'La mejor laptop que he tenido, vale cada centavo', CURRENT_TIMESTAMP - INTERVAL '5 days'),
(4, 3, 'auth0|user1', 5, 'Monitor perfecto para trabajo, aumentó mi productividad', CURRENT_TIMESTAMP - INTERVAL '15 days'),
(5, 7, 'auth0|user2', 5, 'Ratón increíble, muy cómodo para largas jornadas', CURRENT_TIMESTAMP - INTERVAL '12 days'),
(6, 9, 'auth0|user3', 5, 'Cancelación de ruido impresionante', CURRENT_TIMESTAMP - INTERVAL '7 days'),
(7, 13, 'auth0|user1', 5, 'iPad Air es perfecta para diseño y entretenimiento', CURRENT_TIMESTAMP - INTERVAL '3 days');

SELECT setval('reviews_id_seq', (SELECT MAX(id) FROM reviews));


-- ====================================================================
-- 2. PEDIDOS_DB - Pedidos y Carritos
-- ====================================================================

\c pedidos_db

-- Limpiar datos existentes
TRUNCATE TABLE pedido_historial CASCADE;
TRUNCATE TABLE detalle_pedido CASCADE;
TRUNCATE TABLE pedidos CASCADE;
TRUNCATE TABLE carrito_items CASCADE;
TRUNCATE TABLE carritos CASCADE;

-- Insertar carritos activos
INSERT INTO carritos (id, usuario_id, total, created_at, updated_at)
VALUES
('auth0|user1', 'auth0|user1', 179.98, CURRENT_TIMESTAMP - INTERVAL '2 hours', CURRENT_TIMESTAMP),
('auth0|user2', 'auth0|user2', 1299.99, CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP);

-- Items en carritos
INSERT INTO carrito_items (carrito_id, producto_id, cantidad, precio)
VALUES
('auth0|user1', 5, 1, 179.99),
('auth0|user2', 1, 1, 1299.99);

-- Insertar pedidos completados
INSERT INTO pedidos (id, usuario_id, estado, total, direccion_envio, metodo_pago, created_at, updated_at)
VALUES
(1, 'auth0|user1', 'ENTREGADO', 1899.98, '{"calle": "Av. Libertador 1234", "comuna": "Providencia", "ciudad": "Santiago", "region": "Metropolitana", "codigoPostal": "7510123"}', 
 'TARJETA_CREDITO', CURRENT_TIMESTAMP - INTERVAL '30 days', CURRENT_TIMESTAMP - INTERVAL '23 days'),

(2, 'auth0|user2', 'ENTREGADO', 799.99, '{"calle": "Calle Merced 567", "comuna": "Santiago Centro", "ciudad": "Santiago", "region": "Metropolitana", "codigoPostal": "8320000"}',
 'TARJETA_DEBITO', CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP - INTERVAL '20 days'),

(3, 'auth0|user3', 'EN_CAMINO', 599.99, '{"calle": "Av. Vitacura 8899", "comuna": "Vitacura", "ciudad": "Santiago", "region": "Metropolitana", "codigoPostal": "7630000"}',
 'TARJETA_CREDITO', CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP - INTERVAL '3 days');

(4, 'auth0|user1', 'PREPARANDO', 699.98, '{"calle": "Av. Libertador 1234", "comuna": "Providencia", "ciudad": "Santiago", "region": "Metropolitana", "codigoPostal": "7510123"}',
 'TARJETA_CREDITO', CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '1 day'),

(5, 'auth0|user2', 'CANCELADO', 399.99, '{"calle": "Calle Merced 567", "comuna": "Santiago Centro", "ciudad": "Santiago", "region": "Metropolitana", "codigoPostal": "8320000"}',
 'TRANSFERENCIA', CURRENT_TIMESTAMP - INTERVAL '15 days', CURRENT_TIMESTAMP - INTERVAL '14 days'),

(6, 'auth0|user3', 'ENTREGADO', 279.98, '{"calle": "Av. Vitacura 8899", "comuna": "Vitacura", "ciudad": "Santiago", "region": "Metropolitana", "codigoPostal": "7630000"}',
 'TARJETA_CREDITO', CURRENT_TIMESTAMP - INTERVAL '40 days', CURRENT_TIMESTAMP - INTERVAL '35 days'),

(7, 'auth0|user1', 'ENTREGADO', 99.99, '{"calle": "Av. Libertador 1234", "comuna": "Providencia", "ciudad": "Santiago", "region": "Metropolitana", "codigoPostal": "7510123"}',
 'TARJETA_DEBITO', CURRENT_TIMESTAMP - INTERVAL '50 days', CURRENT_TIMESTAMP - INTERVAL '45 days'),

(8, 'auth0|user2', 'PENDIENTE', 169.99, '{"calle": "Calle Merced 567", "comuna": "Santiago Centro", "ciudad": "Santiago", "region": "Metropolitana", "codigoPostal": "8320000"}',
 'TARJETA_CREDITO', CURRENT_TIMESTAMP - INTERVAL '1 hour', CURRENT_TIMESTAMP - INTERVAL '30 minutes');

SELECT setval('pedidos_id_seq', (SELECT MAX(id) FROM pedidos));

-- Detalles de pedidos
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio_unitario, subtotal)
VALUES
-- Pedido 1
(1, 1, 1, 1299.99, 1299.99),
(1, 3, 1, 599.99, 599.99),
-- Pedido 2
(2, 4, 1, 799.99, 799.99),
-- Pedido 3
(3, 3, 1, 599.99, 599.99),
-- Pedido 4
(4, 7, 2, 99.99, 199.98),
(4, 11, 1, 79.99, 79.99),
(4, 16, 3, 139.99, 419.97),
-- Pedido 5 (cancelado)
(5, 9, 1, 399.99, 399.99),
-- Pedido 6
(6, 10, 1, 99.99, 99.99),
(6, 8, 1, 69.99, 69.99),
(6, 11, 1, 79.99, 79.99),
(6, 17, 1, 79.99, 79.99),
-- Pedido 7
(7, 7, 1, 99.99, 99.99),
-- Pedido 8
(8, 12, 1, 169.99, 169.99);

-- Historial de estados
INSERT INTO pedido_historial (pedido_id, estado_anterior, estado_nuevo, comentario, created_at)
VALUES
-- Pedido 1 (completado)
(1, NULL, 'PENDIENTE', 'Pedido creado', CURRENT_TIMESTAMP - INTERVAL '30 days'),
(1, 'PENDIENTE', 'CONFIRMADO', 'Pago confirmado', CURRENT_TIMESTAMP - INTERVAL '29 days'),
(1, 'CONFIRMADO', 'PREPARANDO', 'Pedido en preparación', CURRENT_TIMESTAMP - INTERVAL '28 days'),
(1, 'PREPARANDO', 'ENVIADO', 'Pedido enviado', CURRENT_TIMESTAMP - INTERVAL '26 days'),
(1, 'ENVIADO', 'EN_CAMINO', 'En camino al destino', CURRENT_TIMESTAMP - INTERVAL '25 days'),
(1, 'EN_CAMINO', 'ENTREGADO', 'Entregado exitosamente', CURRENT_TIMESTAMP - INTERVAL '23 days'),
-- Pedido 2 (completado)
(2, NULL, 'PENDIENTE', 'Pedido creado', CURRENT_TIMESTAMP - INTERVAL '25 days'),
(2, 'PENDIENTE', 'CONFIRMADO', 'Pago confirmado', CURRENT_TIMESTAMP - INTERVAL '24 days'),
(2, 'CONFIRMADO', 'PREPARANDO', 'Pedido en preparación', CURRENT_TIMESTAMP - INTERVAL '23 days'),
(2, 'PREPARANDO', 'ENVIADO', 'Pedido enviado', CURRENT_TIMESTAMP - INTERVAL '22 days'),
(2, 'ENVIADO', 'EN_CAMINO', 'En camino al destino', CURRENT_TIMESTAMP - INTERVAL '21 days'),
(2, 'EN_CAMINO', 'ENTREGADO', 'Entregado exitosamente', CURRENT_TIMESTAMP - INTERVAL '20 days');

-- Pedido 3 (en camino)
(3, NULL, 'PENDIENTE', 'Pedido creado', CURRENT_TIMESTAMP - INTERVAL '5 days'),
(3, 'PENDIENTE', 'CONFIRMADO', 'Pago confirmado', CURRENT_TIMESTAMP - INTERVAL '4 days'),
(3, 'CONFIRMADO', 'PREPARANDO', 'Pedido en preparación', CURRENT_TIMESTAMP - INTERVAL '4 days'),
(3, 'PREPARANDO', 'ENVIADO', 'Pedido enviado', CURRENT_TIMESTAMP - INTERVAL '3 days'),
(3, 'ENVIADO', 'EN_CAMINO', 'En camino al destino', CURRENT_TIMESTAMP - INTERVAL '3 days'),
-- Pedido 4 (preparando)
(4, NULL, 'PENDIENTE', 'Pedido creado', CURRENT_TIMESTAMP - INTERVAL '2 days'),
(4, 'PENDIENTE', 'CONFIRMADO', 'Pago confirmado', CURRENT_TIMESTAMP - INTERVAL '1 day'),
(4, 'CONFIRMADO', 'PREPARANDO', 'Pedido en preparación', CURRENT_TIMESTAMP - INTERVAL '1 day'),
-- Pedido 5 (cancelado)
(5, NULL, 'PENDIENTE', 'Pedido creado', CURRENT_TIMESTAMP - INTERVAL '15 days'),
(5, 'PENDIENTE', 'CANCELADO', 'Cancelado por cliente', CURRENT_TIMESTAMP - INTERVAL '14 days'),
-- Pedido 8 (pendiente)
(8, NULL, 'PENDIENTE', 'Pedido creado', CURRENT_TIMESTAMP - INTERVAL '30 minutes');

-- ====================================================================
-- 3. ENVIO_DB - Envíos
-- ====================================================================

\c envio_db

-- Limpiar datos existentes
TRUNCATE TABLE historial_envio CASCADE;
TRUNCATE TABLE envios CASCADE;

-- Insertar envíos
INSERT INTO envios (id, pedido_id, codigo_tracking, destinatario, direccion_completa, ciudad, estado, 
                   chofer_id, fecha_estimada, fecha_entrega, created_at, updated_at)
VALUES
(1, 1, 'SMART-2026-001-ABC', 'Juan Pérez', 'Av. Libertador 1234, Providencia', 'Santiago', 'ENTREGADO',
 'auth0|chofer1', CURRENT_TIMESTAMP - INTERVAL '24 days', CURRENT_TIMESTAMP - INTERVAL '23 days',
 CURRENT_TIMESTAMP - INTERVAL '26 days', CURRENT_TIMESTAMP - INTERVAL '23 days'),

(2, 2, 'SMART-2026-002-DEF', 'María García', 'Calle Merced 567, Santiago Centro', 'Santiago', 'ENTREGADO',
 'auth0|chofer2', CURRENT_TIMESTAMP - INTERVAL '21 days', CURRENT_TIMESTAMP - INTERVAL '20 days',
 CURRENT_TIMESTAMP - INTERVAL '22 days', CURRENT_TIMESTAMP - INTERVAL '20 days'),

(3, 3, 'SMART-2026-003-GHI', 'Carlos López', 'Av. Vitacura 8899, Vitacura', 'Santiago', 'EN_CAMINO',
 'auth0|chofer1', CURRENT_TIMESTAMP + INTERVAL '1 day', NULL,
 CURRENT_TIMESTAMP - INTERVAL '3 days', CURRENT_TIMESTAMP - INTERVAL '12 hours'),

(4, 4, 'SMART-2026-004-JKL', 'Ana Martínez', 'Av. Libertador 1234, Providencia', 'Santiago', 'PREPARANDO',
 NULL, CURRENT_TIMESTAMP + INTERVAL '3 days', NULL,
 CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day'),

(6, 6, 'SMART-2026-006-MNO', 'Pedro Rodríguez', 'Av. Vitacura 8899, Vitacura', 'Santiago', 'ENTREGADO',
 'auth0|chofer2', CURRENT_TIMESTAMP - INTERVAL '36 days', CURRENT_TIMESTAMP - INTERVAL '35 days',
 CURRENT_TIMESTAMP - INTERVAL '38 days', CURRENT_TIMESTAMP - INTERVAL '35 days'),

(7, 7, 'SMART-2026-007-PQR', 'Laura Fernández', 'Av. Libertador 1234, Providencia', 'Santiago', 'ENTREGADO',
 'auth0|chofer1', CURRENT_TIMESTAMP - INTERVAL '46 days', CURRENT_TIMESTAMP - INTERVAL '45 days',
 CURRENT_TIMESTAMP - INTERVAL '48 days', CURRENT_TIMESTAMP - INTERVAL '45 days'),

(8, 8, 'SMART-2026-008-STU', 'Roberto Sánchez', 'Calle Merced 567, Santiago Centro', 'Santiago', 'PENDIENTE',
 NULL, CURRENT_TIMESTAMP + INTERVAL '5 days', NULL,
 CURRENT_TIMESTAMP - INTERVAL '30 minutes', CURRENT_TIMESTAMP - INTERVAL '30 minutes');

SELECT setval('envios_id_seq', (SELECT MAX(id) FROM envios));


-- Historial de envíos
INSERT INTO historial_envio (envio_id, estado_anterior, estado_nuevo, descripcion, ubicacion, fecha)
VALUES
-- Envío 1 (completado)
(1, NULL, 'PENDIENTE', 'Envío creado', 'Centro de distribución', CURRENT_TIMESTAMP - INTERVAL '26 days'),
(1, 'PENDIENTE', 'PREPARANDO', 'Preparando paquete', 'Centro de distribución', CURRENT_TIMESTAMP - INTERVAL '26 days'),
(1, 'PREPARANDO', 'ENVIADO', 'Paquete despachado', 'Centro de distribución', CURRENT_TIMESTAMP - INTERVAL '25 days'),
(1, 'ENVIADO', 'EN_CAMINO', 'En camino al destino', 'En ruta', CURRENT_TIMESTAMP - INTERVAL '24 days'),
(1, 'EN_CAMINO', 'ENTREGADO', 'Entregado exitosamente', 'Av. Libertador 1234', CURRENT_TIMESTAMP - INTERVAL '23 days'),
-- Envío 2 (completado)
(2, NULL, 'PENDIENTE', 'Envío creado', 'Centro de distribución', CURRENT_TIMESTAMP - INTERVAL '22 days'),
(2, 'PENDIENTE', 'PREPARANDO', 'Preparando paquete', 'Centro de distribución', CURRENT_TIMESTAMP - INTERVAL '22 days'),
(2, 'PREPARANDO', 'ENVIADO', 'Paquete despachado', 'Centro de distribución', CURRENT_TIMESTAMP - INTERVAL '21 days'),
(2, 'ENVIADO', 'EN_CAMINO', 'En camino al destino', 'En ruta', CURRENT_TIMESTAMP - INTERVAL '20 days'),
(2, 'EN_CAMINO', 'ENTREGADO', 'Entregado exitosamente', 'Calle Merced 567', CURRENT_TIMESTAMP - INTERVAL '20 days'),
-- Envío 3 (en camino)
(3, NULL, 'PENDIENTE', 'Envío creado', 'Centro de distribución', CURRENT_TIMESTAMP - INTERVAL '3 days'),
(3, 'PENDIENTE', 'PREPARANDO', 'Preparando paquete', 'Centro de distribución', CURRENT_TIMESTAMP - INTERVAL '3 days'),
(3, 'PREPARANDO', 'ENVIADO', 'Paquete despachado', 'Centro de distribución', CURRENT_TIMESTAMP - INTERVAL '2 days'),
(3, 'ENVIADO', 'EN_CAMINO', 'En camino al destino', 'En ruta', CURRENT_TIMESTAMP - INTERVAL '12 hours'),
(3, 'EN_CAMINO', 'EN_CAMINO', 'Llegó a punto intermedio', 'Providencia', CURRENT_TIMESTAMP - INTERVAL '6 hours'),
-- Envío 4 (preparando)
(4, NULL, 'PENDIENTE', 'Envío creado', 'Centro de distribución', CURRENT_TIMESTAMP - INTERVAL '1 day'),
(4, 'PENDIENTE', 'PREPARANDO', 'Preparando paquete', 'Centro de distribución', CURRENT_TIMESTAMP - INTERVAL '1 day'),
-- Envío 8 (pendiente)
(8, NULL, 'PENDIENTE', 'Envío creado', 'Centro de distribución', CURRENT_TIMESTAMP - INTERVAL '30 minutes');

-- ====================================================================
-- 4. USUARIOS_DB - Usuarios
-- ====================================================================

\c usuarios_db

-- Limpiar datos existentes
TRUNCATE TABLE usuarios CASCADE;

-- Insertar usuarios de ejemplo
-- Nota: En producción, estos usuarios deberían estar en Auth0
-- Aquí los insertamos para tener datos coherentes en el sistema
INSERT INTO usuarios (id, email, nombre, apellido, telefono, direccion, activo, created_at, updated_at)
VALUES
('auth0|user1', 'juan.perez@example.com', 'Juan', 'Pérez', '+56912345678', 
 '{"calle": "Av. Libertador 1234", "comuna": "Providencia", "ciudad": "Santiago", "region": "Metropolitana", "codigoPostal": "7510123"}',
 true, CURRENT_TIMESTAMP - INTERVAL '60 days', CURRENT_TIMESTAMP),

('auth0|user2', 'maria.garcia@example.com', 'María', 'García', '+56987654321',
 '{"calle": "Calle Merced 567", "comuna": "Santiago Centro", "ciudad": "Santiago", "region": "Metropolitana", "codigoPostal": "8320000"}',
 true, CURRENT_TIMESTAMP - INTERVAL '50 days', CURRENT_TIMESTAMP),

('auth0|user3', 'carlos.lopez@example.com', 'Carlos', 'López', '+56911223344',
 '{"calle": "Av. Vitacura 8899", "comuna": "Vitacura", "ciudad": "Santiago", "region": "Metropolitana", "codigoPostal": "7630000"}',
 true, CURRENT_TIMESTAMP - INTERVAL '45 days', CURRENT_TIMESTAMP),

('auth0|seller1', 'vendedor1@smartlogix.com', 'Roberto', 'Vendedor', '+56922334455',
 '{"calle": "Av. Apoquindo 3000", "comuna": "Las Condes", "ciudad": "Santiago", "region": "Metropolitana", "codigoPostal": "7550000"}',
 true, CURRENT_TIMESTAMP - INTERVAL '180 days', CURRENT_TIMESTAMP),

('auth0|seller2', 'vendedor2@smartlogix.com', 'Patricia', 'Comercial', '+56933445566',
 '{"calle": "Av. Kennedy 5600", "comuna": "Las Condes", "ciudad": "Santiago", "region": "Metropolitana", "codigoPostal": "7560000"}',
 true, CURRENT_TIMESTAMP - INTERVAL '150 days', CURRENT_TIMESTAMP),

('auth0|chofer1', 'chofer1@smartlogix.com', 'Diego', 'Conductor', '+56944556677',
 '{"calle": "Calle Puente 890", "comuna": "Maipú", "ciudad": "Santiago", "region": "Metropolitana", "codigoPostal": "9250000"}',
 true, CURRENT_TIMESTAMP - INTERVAL '120 days', CURRENT_TIMESTAMP),

('auth0|chofer2', 'chofer2@smartlogix.com', 'Andrea', 'Transportista', '+56955667788',
 '{"calle": "Av. Pajaritos 1200", "comuna": "Maipú", "ciudad": "Santiago", "region": "Metropolitana", "codigoPostal": "9260000"}',
 true, CURRENT_TIMESTAMP - INTERVAL '100 days', CURRENT_TIMESTAMP),

('auth0|admin1', 'admin@smartlogix.com', 'Administrador', 'Principal', '+56900000000',
 '{"calle": "Av. Providencia 1000", "comuna": "Providencia", "ciudad": "Santiago", "region": "Metropolitana", "codigoPostal": "7500000"}',
 true, CURRENT_TIMESTAMP - INTERVAL '365 days', CURRENT_TIMESTAMP);

-- ====================================================================
-- FIN DEL SCRIPT
-- ====================================================================
