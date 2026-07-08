-- ====================================================================
-- SCRIPT DE DATOS DE PRUEBA PARA SMARTLOGIX
-- ====================================================================

-- ====================================================================
-- 1. INVENTORY_DB - Productos
-- ====================================================================

\c inventory_db

-- Limpiar datos existentes (en orden correcto para evitar problemas de FK)
DELETE FROM producto_comentarios;
DELETE FROM producto_imagenes;
DELETE FROM inventory_reservas_stock;
DELETE FROM productos;
DELETE FROM banners;

-- Insertar productos realistas
INSERT INTO productos (nombre, descripcion, categoria, precio, stock, destacado, oferta, nuevo, 
                      descuento_porcentaje, marca, modelo, rating_promedio, total_ratings, cantidad_vendidos,
                      garantia, peso, dimensiones, vendedor_id, estado, fecha_creacion, fecha_actualizacion)
VALUES
-- Laptops
('Laptop Dell XPS 15', 'Potente laptop para profesionales con pantalla 4K OLED', 'Laptops', 1299.99, 25, 
 true, false, false, 0, 'Dell', 'XPS 15 9520', 4.7, 142, 89, '2 años', '2.1 kg', '35.7 x 23.5 x 1.8 cm', 
 'auth0|seller1', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
 
('MacBook Pro 14"', 'MacBook Pro con chip M2 Pro para máximo rendimiento', 'Laptops', 1999.99, 15,
 true, true, false, 15, 'Apple', 'MacBook Pro 14 M2', 4.9, 234, 156, '1 año', '1.6 kg', '31.26 x 22.12 x 1.55 cm',
 'auth0|seller1', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Lenovo ThinkPad X1', 'Ultrabook empresarial con seguridad avanzada', 'Laptops', 1499.99, 18,
 false, false, true, 0, 'Lenovo', 'ThinkPad X1 Carbon', 4.6, 98, 67, '3 años', '1.1 kg', '31.5 x 22.1 x 1.49 cm',
 'auth0|seller2', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Monitores
INSERT INTO productos (nombre, descripcion, categoria, precio, stock, destacado, oferta, nuevo,
                      descuento_porcentaje, marca, modelo, rating_promedio, total_ratings, cantidad_vendidos,
                      garantia, peso, dimensiones, vendedor_id, estado, fecha_creacion, fecha_actualizacion)
VALUES
('Monitor LG UltraWide 34"', 'Monitor ultrawide curvo para productividad extrema', 'Monitores', 599.99, 40,
 true, false, true, 0, 'LG', '34WN80C-B', 4.6, 98, 67, '3 años', '6.8 kg', '81.5 x 56.2 x 26.7 cm',
 'auth0|seller2', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Samsung Odyssey G7 27"', 'Monitor gaming curvo 240Hz con HDR', 'Monitores', 799.99, 18,
 false, true, false, 20, 'Samsung', 'Odyssey G7', 4.8, 187, 134, '1 año', '8.2 kg', '61.4 x 46.9 x 29.1 cm',
 'auth0|seller2', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('ASUS ProArt 27"', 'Monitor profesional 4K con calibración de color', 'Monitores', 549.99, 22,
 false, false, false, 0, 'ASUS', 'ProArt PA278QV', 4.5, 76, 54, '3 años', '5.4 kg', '61.3 x 52.3 x 20.5 cm',
 'auth0|seller1', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Periféricos - Teclados
('Teclado Mecánico Corsair K95', 'Teclado gaming mecánico RGB con switches Cherry MX', 'Periféricos', 179.99, 60,
 false, false, true, 0, 'Corsair', 'K95 RGB Platinum', 4.5, 203, 178, '2 años', '1.3 kg', '46.5 x 17 x 3.9 cm',
 'auth0|seller1', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Logitech MX Keys', 'Teclado inalámbrico premium para profesionales', 'Periféricos', 99.99, 85,
 true, false, false, 0, 'Logitech', 'MX Keys', 4.7, 412, 389, '1 año', '0.8 kg', '43.0 x 13.1 x 2.0 cm',
 'auth0|seller1', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Keychron K2', 'Teclado mecánico compacto inalámbrico', 'Periféricos', 89.99, 95,
 false, true, true, 10, 'Keychron', 'K2 V2', 4.6, 289, 245, '1 año', '0.6 kg', '35.9 x 12.7 x 4.0 cm',
 'auth0|seller2', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Periféricos - Ratones
('Logitech MX Master 3', 'Ratón inalámbrico ergonómico con scroll horizontal', 'Periféricos', 99.99, 120,
 true, true, false, 10, 'Logitech', 'MX Master 3', 4.8, 567, 523, '1 año', '0.14 kg', '12.4 x 8.4 x 5.1 cm',
 'auth0|seller2', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Razer DeathAdder V2', 'Ratón gaming con sensor óptico de 20K DPI', 'Periféricos', 69.99, 95,
 false, false, true, 0, 'Razer', 'DeathAdder V2', 4.6, 289, 245, '2 años', '0.08 kg', '12.7 x 6.1 x 4.3 cm',
 'auth0|seller2', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Logitech G Pro X', 'Ratón ultraligero para gaming profesional', 'Periféricos', 129.99, 48,
 false, false, false, 0, 'Logitech', 'G Pro X Superlight', 4.7, 334, 298, '2 años', '0.063 kg', '12.5 x 6.3 x 4.0 cm',
 'auth0|seller1', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Audio
INSERT INTO productos (nombre, descripcion, categoria, precio, stock, destacado, oferta, nuevo,
                      descuento_porcentaje, marca, modelo, rating_promedio, total_ratings, cantidad_vendidos,
                      garantia, peso, dimensiones, vendedor_id, estado, fecha_creacion, fecha_actualizacion)
VALUES
('Sony WH-1000XM5', 'Auriculares con cancelación de ruido premium líder del mercado', 'Audio', 399.99, 45,
 true, false, true, 0, 'Sony', 'WH-1000XM5', 4.9, 723, 689, '1 año', '0.25 kg', '25.4 x 21.4 x 8.2 cm',
 'auth0|seller1', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('HyperX Cloud II', 'Auriculares gaming con sonido surround 7.1', 'Audio', 99.99, 70,
 false, true, false, 15, 'HyperX', 'Cloud II', 4.7, 445, 412, '2 años', '0.32 kg', '17.2 x 9.3 x 18.4 cm',
 'auth0|seller2', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Bose QuietComfort 45', 'Auriculares inalámbricos con cancelación de ruido', 'Audio', 329.99, 33,
 true, false, false, 0, 'Bose', 'QC45', 4.8, 456, 423, '1 año', '0.24 kg', '18.4 x 15.2 x 7.6 cm',
 'auth0|seller1', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Logitech C920 HD Pro', 'Webcam Full HD 1080p con autoenfoque', 'Periféricos', 79.99, 110,
 false, false, false, 0, 'Logitech', 'C920', 4.5, 892, 834, '2 años', '0.16 kg', '9.4 x 4.3 x 7.1 cm',
 'auth0|seller1', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Elgato Facecam', 'Webcam profesional 1080p60 para streaming', 'Periféricos', 169.99, 32,
 true, false, true, 0, 'Elgato', 'Facecam', 4.6, 178, 145, '2 años', '0.15 kg', '5.8 x 5.8 x 6.6 cm',
 'auth0|seller2', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Tablets
('iPad Air M1', 'Tablet potente con chip M1 y pantalla Liquid Retina', 'Tablets', 599.99, 35,
 true, false, false, 0, 'Apple', 'iPad Air 5', 4.8, 356, 312, '1 año', '0.46 kg', '24.76 x 17.85 x 0.61 cm',
 'auth0|seller1', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Samsung Galaxy Tab S8', 'Tablet Android premium con S Pen incluido', 'Tablets', 699.99, 28,
 false, true, false, 10, 'Samsung', 'Galaxy Tab S8', 4.6, 234, 198, '1 año', '0.50 kg', '25.3 x 16.5 x 0.64 cm',
 'auth0|seller2', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Impresoras y Almacenamiento
('HP LaserJet Pro M404dn', 'Impresora láser monocromática de red', 'Impresoras', 279.99, 22,
 false, false, true, 0, 'HP', 'LaserJet Pro M404dn', 4.4, 167, 142, '1 año', '10.2 kg', '37.4 x 35.9 x 31.8 cm',
 'auth0|seller1', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('Samsung SSD 1TB 980 PRO', 'SSD NVMe PCIe 4.0 ultra rápido', 'Almacenamiento', 139.99, 150,
 true, true, false, 25, 'Samsung', '980 PRO', 4.9, 1234, 1089, '5 años', '0.008 kg', '8.0 x 2.2 x 0.23 cm',
 'auth0|seller1', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('WD My Passport 2TB', 'Disco duro externo portátil USB 3.2', 'Almacenamiento', 79.99, 200,
 false, false, false, 0, 'Western Digital', 'My Passport', 4.5, 789, 723, '2 años', '0.17 kg', '10.7 x 7.5 x 1.9 cm',
 'auth0|seller2', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

('SanDisk Extreme Pro 512GB', 'SSD portátil resistente con velocidades de 2000MB/s', 'Almacenamiento', 119.99, 88,
 false, true, true, 15, 'SanDisk', 'Extreme Pro', 4.7, 445, 389, '5 años', '0.08 kg', '11.0 x 5.7 x 1.0 cm',
 'auth0|seller1', 'ACTIVO', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insertar imágenes para los productos
INSERT INTO producto_imagenes (producto_id, url, es_principal, orden)
SELECT p.id, 
       CASE p.nombre
           WHEN 'Laptop Dell XPS 15' THEN 'https://images.unsplash.com/photo-1593642632823-8f785ba67e45'
           WHEN 'MacBook Pro 14"' THEN 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8'
           WHEN 'Lenovo ThinkPad X1' THEN 'https://images.unsplash.com/photo-1588872657578-7efd1f1555ed'
           WHEN 'Monitor LG UltraWide 34"' THEN 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf'
           WHEN 'Samsung Odyssey G7 27"' THEN 'https://images.unsplash.com/photo-1593305841991-05c297ba4575'
           WHEN 'ASUS ProArt 27"' THEN 'https://images.unsplash.com/photo-1585792180666-f7347c490ee2'
           WHEN 'Teclado Mecánico Corsair K95' THEN 'https://images.unsplash.com/photo-1595225476474-87563907a212'
           WHEN 'Logitech MX Keys' THEN 'https://images.unsplash.com/photo-1618384887929-16ec33fab9ef'
           WHEN 'Keychron K2' THEN 'https://images.unsplash.com/photo-1587829741301-dc798b83add3'
           WHEN 'Logitech MX Master 3' THEN 'https://images.unsplash.com/photo-1527814050087-3793815479db'
           WHEN 'Razer DeathAdder V2' THEN 'https://images.unsplash.com/photo-1563297007-0686b7003af7'
           WHEN 'Logitech G Pro X' THEN 'https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7'
           WHEN 'Sony WH-1000XM5' THEN 'https://images.unsplash.com/photo-1546435770-a3e426bf472b'
           WHEN 'HyperX Cloud II' THEN 'https://images.unsplash.com/photo-1577174881658-0f30157d96c4'
           WHEN 'Bose QuietComfort 45' THEN 'https://images.unsplash.com/photo-1484704849700-f032a568e944'
           WHEN 'Logitech C920 HD Pro' THEN 'https://images.unsplash.com/photo-1570046057387-d63958dc7bd5'
           WHEN 'Elgato Facecam' THEN 'https://images.unsplash.com/photo-1517694712202-14dd9538aa97'
           WHEN 'iPad Air M1' THEN 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0'
           WHEN 'Samsung Galaxy Tab S8' THEN 'https://images.unsplash.com/photo-1561154464-82e9adf32764'
           WHEN 'HP LaserJet Pro M404dn' THEN 'https://images.unsplash.com/photo-1612815154858-60aa4c59eaa6'
           WHEN 'Samsung SSD 1TB 980 PRO' THEN 'https://images.unsplash.com/photo-1597872200969-2b65d56bd16b'
           WHEN 'WD My Passport 2TB' THEN 'https://images.unsplash.com/photo-1597872200969-2b65d56bd16b'
           WHEN 'SanDisk Extreme Pro 512GB' THEN 'https://images.unsplash.com/photo-1597872200969-2b65d56bd16b'
       END,
       true, 1
FROM productos p;

-- Insertar banners
INSERT INTO banners (titulo, descripcion, imagen_url, link_url, activo, orden, fecha_inicio, fecha_fin)
VALUES
('Ofertas de Verano', 'Hasta 30% de descuento en laptops y monitores seleccionados', 
 'https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da', '/catalogo?categoria=Laptops', 
 true, 1, CURRENT_DATE, CURRENT_DATE + INTERVAL '30 days'),
 
('Nuevo: MacBook Pro M2', 'Descubre la potencia del chip M2 Pro en el nuevo MacBook Pro', 
 'https://images.unsplash.com/photo-1517694712202-14dd9538aa97', '/producto/2', 
 true, 2, CURRENT_DATE, CURRENT_DATE + INTERVAL '60 days'),
 
('Gaming Setup Completo', 'Arma tu estación gaming perfecta con nuestros periféricos', 
 'https://images.unsplash.com/photo-1593305841991-05c297ba4575', '/catalogo?categoria=Periféricos', 
 true, 3, CURRENT_DATE, CURRENT_DATE + INTERVAL '45 days'),

('Almacenamiento Ultra Rápido', 'SSDs NVMe con hasta 25% de descuento', 
 'https://images.unsplash.com/photo-1597872200969-2b65d56bd16b', '/catalogo?categoria=Almacenamiento', 
 true, 4, CURRENT_DATE, CURRENT_DATE + INTERVAL '20 days');

-- Insertar comentarios
INSERT INTO producto_comentarios (producto_id, usuario_id, calificacion, comentario, fecha_creacion)
SELECT p.id, 'auth0|user1', 5, 'Excelente producto, superó mis expectativas', CURRENT_TIMESTAMP - INTERVAL '10 days'
FROM productos p WHERE p.nombre = 'Laptop Dell XPS 15'
UNION ALL
SELECT p.id, 'auth0|user2', 4, 'Buena calidad pero un poco cara', CURRENT_TIMESTAMP - INTERVAL '8 days'
FROM productos p WHERE p.nombre = 'Laptop Dell XPS 15'
UNION ALL
SELECT p.id, 'auth0|user3', 5, 'La mejor inversión que he hecho', CURRENT_TIMESTAMP - INTERVAL '5 days'
FROM productos p WHERE p.nombre = 'MacBook Pro 14"'
UNION ALL
SELECT p.id, 'auth0|user1', 5, 'Perfecto para mi trabajo, muy productivo', CURRENT_TIMESTAMP - INTERVAL '15 days'
FROM productos p WHERE p.nombre = 'Monitor LG UltraWide 34"'
UNION ALL
SELECT p.id, 'auth0|user2', 5, 'Increíble comodidad para largas jornadas', CURRENT_TIMESTAMP - INTERVAL '12 days'
FROM productos p WHERE p.nombre = 'Logitech MX Master 3'
UNION ALL
SELECT p.id, 'auth0|user3', 5, 'Cancelación de ruido impresionante', CURRENT_TIMESTAMP - INTERVAL '7 days'
FROM productos p WHERE p.nombre = 'Sony WH-1000XM5'
UNION ALL
SELECT p.id, 'auth0|user1', 5, 'Perfecta para diseño y entretenimiento', CURRENT_TIMESTAMP - INTERVAL '3 days'
FROM productos p WHERE p.nombre = 'iPad Air M1';


-- ====================================================================
-- 2. PEDIDOS_DB - Pedidos y Carritos  
-- ====================================================================

\c pedidos_db

-- Limpiar datos existentes
DELETE FROM pedido_historial;
DELETE FROM detalle_pedido;
DELETE FROM pedidos;
DELETE FROM carrito_items;
DELETE FROM carritos;

-- Insertar carritos activos
INSERT INTO carritos (usuario_id, estado, total, fecha_creacion, fecha_actualizacion)
VALUES
('auth0|user1', 'ACTIVO', 179.99, CURRENT_TIMESTAMP - INTERVAL '2 hours', CURRENT_TIMESTAMP),
('auth0|user2', 'ACTIVO', 1299.99, CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP);

-- Items en carritos (usamos IDs de productos que sabemos que existen)
INSERT INTO carrito_items (carrito_id, producto_id, cantidad, precio)
SELECT c.id, 7, 1, 179.99 FROM carritos c WHERE c.usuario_id = 'auth0|user1'
UNION ALL
SELECT c.id, 1, 1, 1299.99 FROM carritos c WHERE c.usuario_id = 'auth0|user2';

-- Insertar pedidos
INSERT INTO pedidos (usuario_id, estado, total, fecha)
VALUES
('auth0|user1', 'ENTREGADO', 1899.98, CURRENT_TIMESTAMP - INTERVAL '30 days'),
('auth0|user2', 'ENTREGADO', 799.99, CURRENT_TIMESTAMP - INTERVAL '25 days'),
('auth0|user3', 'ENVIADO', 599.99, CURRENT_TIMESTAMP - INTERVAL '5 days'),
('auth0|user1', 'EN_PREPARACION', 699.98, CURRENT_TIMESTAMP - INTERVAL '2 days'),
('auth0|user2', 'CANCELADO', 399.99, CURRENT_TIMESTAMP - INTERVAL '15 days'),
('auth0|user3', 'ENTREGADO', 279.98, CURRENT_TIMESTAMP - INTERVAL '40 days'),
('auth0|user1', 'ENTREGADO', 99.99, CURRENT_TIMESTAMP - INTERVAL '50 days'),
('auth0|user2', 'PENDIENTE', 169.99, CURRENT_TIMESTAMP - INTERVAL '1 hour');

-- Detalles de pedidos (pedido 1)
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio, vendedor_id)
SELECT 1, p.id, 1, 1299.99, 'auth0|seller1' FROM productos p WHERE p.nombre = 'Laptop Dell XPS 15'
UNION ALL
SELECT 1, p.id, 1, 599.99, 'auth0|seller2' FROM productos p WHERE p.nombre = 'Monitor LG UltraWide 34"';

-- Pedido 2
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio, vendedor_id)
SELECT 2, p.id, 1, 799.99, 'auth0|seller2' FROM productos p WHERE p.nombre = 'Samsung Odyssey G7 27"';

-- Pedido 3
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio, vendedor_id)
SELECT 3, p.id, 1, 599.99, 'auth0|seller2' FROM productos p WHERE p.nombre = 'Monitor LG UltraWide 34"';

-- Pedido 4
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio, vendedor_id)
SELECT 4, p.id, 2, 99.99, 'auth0|seller2' FROM productos p WHERE p.nombre = 'Logitech MX Master 3'
UNION ALL
SELECT 4, p.id, 1, 79.99, 'auth0|seller1' FROM productos p WHERE p.nombre = 'Logitech C920 HD Pro'
UNION ALL
SELECT 4, p.id, 3, 139.99, 'auth0|seller1' FROM productos p WHERE p.nombre = 'Samsung SSD 1TB 980 PRO';

-- Pedido 5 (cancelado)
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio, vendedor_id)
SELECT 5, p.id, 1, 399.99, 'auth0|seller1' FROM productos p WHERE p.nombre = 'Sony WH-1000XM5';

-- Pedido 6
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio, vendedor_id)
SELECT 6, p.id, 1, 99.99, 'auth0|seller2' FROM productos p WHERE p.nombre = 'HyperX Cloud II'
UNION ALL
SELECT 6, p.id, 1, 69.99, 'auth0|seller2' FROM productos p WHERE p.nombre = 'Razer DeathAdder V2'
UNION ALL
SELECT 6, p.id, 1, 79.99, 'auth0|seller2' FROM productos p WHERE p.nombre = 'WD My Passport 2TB';

-- Pedido 7
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio, vendedor_id)
SELECT 7, p.id, 1, 99.99, 'auth0|seller2' FROM productos p WHERE p.nombre = 'Logitech MX Master 3';

-- Pedido 8
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio, vendedor_id)
SELECT 8, p.id, 1, 169.99, 'auth0|seller2' FROM productos p WHERE p.nombre = 'Elgato Facecam';


-- Historial de pedidos
INSERT INTO pedido_historial (pedido_id, estado_anterior, estado_nuevo, fecha)
VALUES
-- Pedido 1 (completado)
(1, NULL, 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '30 days'),
(1, 'PENDIENTE', 'PAGADO', CURRENT_TIMESTAMP - INTERVAL '29 days'),
(1, 'PAGADO', 'EN_PREPARACION', CURRENT_TIMESTAMP - INTERVAL '28 days'),
(1, 'EN_PREPARACION', 'ENVIADO', CURRENT_TIMESTAMP - INTERVAL '26 days'),
(1, 'ENVIADO', 'ENTREGADO', CURRENT_TIMESTAMP - INTERVAL '23 days'),
-- Pedido 2 (completado)
(2, NULL, 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '25 days'),
(2, 'PENDIENTE', 'PAGADO', CURRENT_TIMESTAMP - INTERVAL '24 days'),
(2, 'PAGADO', 'EN_PREPARACION', CURRENT_TIMESTAMP - INTERVAL '23 days'),
(2, 'EN_PREPARACION', 'ENVIADO', CURRENT_TIMESTAMP - INTERVAL '22 days'),
(2, 'ENVIADO', 'ENTREGADO', CURRENT_TIMESTAMP - INTERVAL '20 days'),
-- Pedido 3 (enviado)
(3, NULL, 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '5 days'),
(3, 'PENDIENTE', 'PAGADO', CURRENT_TIMESTAMP - INTERVAL '4 days'),
(3, 'PAGADO', 'EN_PREPARACION', CURRENT_TIMESTAMP - INTERVAL '4 days'),
(3, 'EN_PREPARACION', 'ENVIADO', CURRENT_TIMESTAMP - INTERVAL '3 days'),
-- Pedido 4 (en preparación)
(4, NULL, 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '2 days'),
(4, 'PENDIENTE', 'PAGADO', CURRENT_TIMESTAMP - INTERVAL '1 day'),
(4, 'PAGADO', 'EN_PREPARACION', CURRENT_TIMESTAMP - INTERVAL '1 day'),
-- Pedido 5 (cancelado)
(5, NULL, 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '15 days'),
(5, 'PENDIENTE', 'CANCELADO', CURRENT_TIMESTAMP - INTERVAL '14 days'),
-- Pedido 8 (pendiente)
(8, NULL, 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '1 hour');

-- ====================================================================
-- 3. ENVIO_DB - Envíos
-- ====================================================================

\c envio_db

-- Limpiar datos existentes
DELETE FROM historial_envios;
DELETE FROM envios;

-- Insertar envíos
INSERT INTO envios (pedido_id, usuario_id, tracking_number, estado, direccion_destino, tipo_envio, 
                   chofer_id, chofer_nombre, fecha_creacion, fecha_estimada_entrega)
VALUES
(1, 'auth0|user1', 'SMART-2026-001-ABC', 'ENTREGADO', 'Av. Libertador 1234, Providencia, Santiago', 'STANDARD',
 'auth0|chofer1', 'Diego Conductor', CURRENT_TIMESTAMP - INTERVAL '26 days', CURRENT_TIMESTAMP - INTERVAL '24 days'),

(2, 'auth0|user2', 'SMART-2026-002-DEF', 'ENTREGADO', 'Calle Merced 567, Santiago Centro, Santiago', 'EXPRESS',
 'auth0|chofer2', 'Andrea Transportista', CURRENT_TIMESTAMP - INTERVAL '22 days', CURRENT_TIMESTAMP - INTERVAL '21 days'),

(3, 'auth0|user3', 'SMART-2026-003-GHI', 'EN_RUTA', 'Av. Vitacura 8899, Vitacura, Santiago', 'STANDARD',
 'auth0|chofer1', 'Diego Conductor', CURRENT_TIMESTAMP - INTERVAL '3 days', CURRENT_TIMESTAMP + INTERVAL '1 day'),

(4, 'auth0|user1', 'SMART-2026-004-JKL', 'PREPARANDO', 'Av. Libertador 1234, Providencia, Santiago', 'STANDARD',
 NULL, NULL, CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP + INTERVAL '3 days'),

(6, 'auth0|user3', 'SMART-2026-006-MNO', 'ENTREGADO', 'Av. Vitacura 8899, Vitacura, Santiago', 'EXPRESS',
 'auth0|chofer2', 'Andrea Transportista', CURRENT_TIMESTAMP - INTERVAL '38 days', CURRENT_TIMESTAMP - INTERVAL '36 days'),

(7, 'auth0|user1', 'SMART-2026-007-PQR', 'ENTREGADO', 'Av. Libertador 1234, Providencia, Santiago', 'STANDARD',
 'auth0|chofer1', 'Diego Conductor', CURRENT_TIMESTAMP - INTERVAL '48 days', CURRENT_TIMESTAMP - INTERVAL '46 days'),

(8, 'auth0|user2', 'SMART-2026-008-STU', 'PENDIENTE', 'Calle Merced 567, Santiago Centro, Santiago', 'STANDARD',
 NULL, NULL, CURRENT_TIMESTAMP - INTERVAL '1 hour', CURRENT_TIMESTAMP + INTERVAL '5 days');


-- Historial de envíos
INSERT INTO historial_envios (envio_id, estado, ubicacion, descripcion, fecha_evento)
VALUES
-- Envío 1 (completado)
(1, 'PENDIENTE', 'Centro de distribución', 'Envío creado y registrado', CURRENT_TIMESTAMP - INTERVAL '26 days'),
(1, 'PREPARANDO', 'Centro de distribución', 'Paquete siendo preparado', CURRENT_TIMESTAMP - INTERVAL '26 days'),
(1, 'EN_RUTA', 'En camino', 'Paquete despachado', CURRENT_TIMESTAMP - INTERVAL '25 days'),
(1, 'EN_RUTA', 'Providencia', 'Llegó a punto intermedio', CURRENT_TIMESTAMP - INTERVAL '24 days'),
(1, 'ENTREGADO', 'Av. Libertador 1234', 'Entregado exitosamente', CURRENT_TIMESTAMP - INTERVAL '23 days'),
-- Envío 2 (completado)
(2, 'PENDIENTE', 'Centro de distribución', 'Envío creado y registrado', CURRENT_TIMESTAMP - INTERVAL '22 days'),
(2, 'PREPARANDO', 'Centro de distribución', 'Paquete siendo preparado', CURRENT_TIMESTAMP - INTERVAL '22 days'),
(2, 'EN_RUTA', 'En camino', 'Paquete despachado - Envío Express', CURRENT_TIMESTAMP - INTERVAL '21 days'),
(2, 'ENTREGADO', 'Calle Merced 567', 'Entregado exitosamente', CURRENT_TIMESTAMP - INTERVAL '20 days'),
-- Envío 3 (en ruta)
(3, 'PENDIENTE', 'Centro de distribución', 'Envío creado y registrado', CURRENT_TIMESTAMP - INTERVAL '3 days'),
(3, 'PREPARANDO', 'Centro de distribución', 'Paquete siendo preparado', CURRENT_TIMESTAMP - INTERVAL '3 days'),
(3, 'EN_RUTA', 'En camino', 'Paquete despachado', CURRENT_TIMESTAMP - INTERVAL '2 days'),
(3, 'EN_RUTA', 'Las Condes', 'En tránsito hacia destino final', CURRENT_TIMESTAMP - INTERVAL '12 hours'),
(3, 'EN_RUTA', 'Vitacura', 'Llegó a punto de entrega', CURRENT_TIMESTAMP - INTERVAL '6 hours'),
-- Envío 4 (preparando)
(4, 'PENDIENTE', 'Centro de distribución', 'Envío creado y registrado', CURRENT_TIMESTAMP - INTERVAL '1 day'),
(4, 'PREPARANDO', 'Centro de distribución', 'Paquete siendo preparado', CURRENT_TIMESTAMP - INTERVAL '1 day'),
-- Envío 6 (completado)
(6, 'PENDIENTE', 'Centro de distribución', 'Envío creado y registrado', CURRENT_TIMESTAMP - INTERVAL '38 days'),
(6, 'PREPARANDO', 'Centro de distribución', 'Paquete siendo preparado', CURRENT_TIMESTAMP - INTERVAL '38 days'),
(6, 'EN_RUTA', 'En camino', 'Paquete despachado - Envío Express', CURRENT_TIMESTAMP - INTERVAL '36 days'),
(6, 'ENTREGADO', 'Av. Vitacura 8899', 'Entregado exitosamente', CURRENT_TIMESTAMP - INTERVAL '35 days'),
-- Envío 7 (completado)
(7, 'PENDIENTE', 'Centro de distribución', 'Envío creado y registrado', CURRENT_TIMESTAMP - INTERVAL '48 days'),
(7, 'PREPARANDO', 'Centro de distribución', 'Paquete siendo preparado', CURRENT_TIMESTAMP - INTERVAL '48 days'),
(7, 'EN_RUTA', 'En camino', 'Paquete despachado', CURRENT_TIMESTAMP - INTERVAL '47 days'),
(7, 'ENTREGADO', 'Av. Libertador 1234', 'Entregado exitosamente', CURRENT_TIMESTAMP - INTERVAL '45 days'),
-- Envío 8 (pendiente)
(8, 'PENDIENTE', 'Centro de distribución', 'Envío creado, esperando procesamiento', CURRENT_TIMESTAMP - INTERVAL '1 hour');

-- ====================================================================
-- 4. USUARIOS_DB - Usuarios
-- ====================================================================

\c usuarios_db

-- Limpiar datos existentes
DELETE FROM usuarios;

-- Insertar usuarios
INSERT INTO usuarios (id, email, nombre, telefono, activo, fecha_creacion, fecha_actualizacion)
VALUES
('auth0|user1', 'juan.perez@example.com', 'Juan Pérez', '+56912345678', true, 
 CURRENT_TIMESTAMP - INTERVAL '60 days', CURRENT_TIMESTAMP),
('auth0|user2', 'maria.garcia@example.com', 'María García', '+56987654321', true, 
 CURRENT_TIMESTAMP - INTERVAL '50 days', CURRENT_TIMESTAMP),
('auth0|user3', 'carlos.lopez@example.com', 'Carlos López', '+56911223344', true, 
 CURRENT_TIMESTAMP - INTERVAL '45 days', CURRENT_TIMESTAMP),
('auth0|seller1', 'vendedor1@smartlogix.com', 'Roberto Vendedor', '+56922334455', true, 
 CURRENT_TIMESTAMP - INTERVAL '180 days', CURRENT_TIMESTAMP),
('auth0|seller2', 'vendedor2@smartlogix.com', 'Patricia Comercial', '+56933445566', true, 
 CURRENT_TIMESTAMP - INTERVAL '150 days', CURRENT_TIMESTAMP),
('auth0|chofer1', 'chofer1@smartlogix.com', 'Diego Conductor', '+56944556677', true, 
 CURRENT_TIMESTAMP - INTERVAL '120 days', CURRENT_TIMESTAMP),
('auth0|chofer2', 'chofer2@smartlogix.com', 'Andrea Transportista', '+56955667788', true, 
 CURRENT_TIMESTAMP - INTERVAL '100 days', CURRENT_TIMESTAMP),
('auth0|admin1', 'admin@smartlogix.com', 'Administrador Principal', '+56900000000', true, 
 CURRENT_TIMESTAMP - INTERVAL '365 days', CURRENT_TIMESTAMP);

-- ====================================================================
-- FIN DEL SCRIPT
-- ====================================================================
