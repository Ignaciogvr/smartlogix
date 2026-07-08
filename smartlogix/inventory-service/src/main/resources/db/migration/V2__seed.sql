-- Limpiar los productos para cargar todo de nuevo
DELETE FROM producto_imagenes;
DELETE FROM productos;

INSERT INTO productos (id, nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, fecha_creacion, fecha_actualizacion, version) VALUES 
-- Periféricos
(1, 'Teclado Mecánico RGB Keychron', 'Teclado mecánico con switches Gateron Red, retroiluminación RGB, layout 75%.', 85000, 48, 'Periféricos', 4.8, 120, 300, 'ACTIVO', NOW(), NOW(), 0),
(2, 'Mouse Logitech G Pro X Superlight', 'Mouse inalámbrico ultraligero para eSports con sensor HERO 25K.', 130000, 30, 'Periféricos', 4.9, 320, 1500, 'ACTIVO', NOW(), NOW(), 0),
(3, 'Webcam Logitech Brio 4K', 'Cámara web Ultra HD 4K con HDR y compatibilidad con Windows Hello.', 199990, 20, 'Periféricos', 4.7, 100, 500, 'ACTIVO', NOW(), NOW(), 0),

-- Monitores
(4, 'Monitor LG UltraGear 27', 'Monitor gaming IPS de 27 pulgadas, 144Hz, 1ms de tiempo de respuesta.', 250000, 10, 'Monitores', 4.9, 200, 450, 'ACTIVO', NOW(), NOW(), 0),
(5, 'Monitor Samsung Odyssey G7', 'Monitor curvo gaming 1440p, 240Hz, 1ms.', 550000, 5, 'Monitores', 4.8, 80, 200, 'ACTIVO', NOW(), NOW(), 0),
(6, 'Monitor Dell UltraSharp 27', 'Monitor 4K USB-C ideal para productividad y diseño gráfico.', 450000, 12, 'Monitores', 4.6, 95, 300, 'ACTIVO', NOW(), NOW(), 0),

-- Muebles
(7, 'Silla Ergonómica Pro Office', 'Silla de oficina con soporte lumbar ajustable, malla transpirable.', 150000, 15, 'Muebles', 4.5, 45, 80, 'ACTIVO', NOW(), NOW(), 0),
(8, 'Escritorio Standing Desk Motorizado', 'Escritorio con ajuste de altura eléctrico y memoria de posiciones.', 299990, 8, 'Muebles', 4.8, 60, 120, 'ACTIVO', NOW(), NOW(), 0),
(9, 'Silla Gamer Secretlab Titan', 'Silla de cuero sintético premium para largas jornadas de juego.', 350000, 10, 'Muebles', 4.9, 150, 400, 'ACTIVO', NOW(), NOW(), 0),

-- Audio
(10, 'Audífonos Sony WH-1000XM5', 'Auriculares inalámbricos con cancelación de ruido.', 300000, 25, 'Audio', 4.8, 500, 2000, 'ACTIVO', NOW(), NOW(), 0),
(11, 'Micrófono Blue Yeti USB', 'Micrófono condensador para streaming, podcast y grabaciones.', 120000, 40, 'Audio', 4.6, 250, 1000, 'ACTIVO', NOW(), NOW(), 0),
(12, 'Altavoces Bose Companion 2', 'Sistema de altavoces multimedia Series III.', 110000, 15, 'Audio', 4.5, 120, 350, 'ACTIVO', NOW(), NOW(), 0),

-- Laptops
(13, 'MacBook Pro M3 Pro 14', 'Laptop de 14 pulgadas con chip M3 Pro, 18GB RAM, 512GB SSD.', 1850000, 8, 'Laptops', 4.9, 300, 800, 'ACTIVO', NOW(), NOW(), 0),
(14, 'ASUS ROG Zephyrus G14', 'Laptop gaming ultradelgada, Ryzen 9, RTX 4060.', 1500000, 6, 'Laptops', 4.7, 180, 400, 'ACTIVO', NOW(), NOW(), 0),
(15, 'Dell XPS 15', 'Laptop premium de 15.6 pulgadas, Core i7, 16GB RAM, RTX 4050.', 1650000, 7, 'Laptops', 4.6, 110, 250, 'ACTIVO', NOW(), NOW(), 0),

-- Accesorios
(16, 'Hub USB-C Anker 8-in-1', 'Adaptador multipuerto con HDMI 4K, lector SD, Ethernet.', 45000, 50, 'Accesorios', 4.7, 400, 3000, 'ACTIVO', NOW(), NOW(), 0),
(17, 'Soporte para Monitor Doble VIVO', 'Soporte articulado de escritorio para dos monitores.', 35000, 35, 'Accesorios', 4.8, 280, 1200, 'ACTIVO', NOW(), NOW(), 0),
(18, 'Funda para Laptop Incase', 'Funda protectora acolchada para laptops de 13 y 14 pulgadas.', 25000, 100, 'Accesorios', 4.5, 90, 800, 'ACTIVO', NOW(), NOW(), 0);

-- Reset sequence for id to avoid conflicts when creating new products via API
SELECT setval('productos_id_seq', 18);

INSERT INTO producto_imagenes (producto_id, imagen_url) VALUES 
-- Periféricos
(1, 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=800&q=80'),
(2, 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=800&q=80'),
(3, 'https://images.unsplash.com/photo-1587825140708-dfaf72ae4b04?w=800&q=80'),
-- Monitores
(4, 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=800&q=80'),
(5, 'https://images.unsplash.com/photo-1585792180666-f7347c490ee2?w=800&q=80'),
(6, 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=800&q=80'),
-- Muebles
(7, 'https://images.unsplash.com/photo-1580480055273-228ff5388ef8?w=800&q=80'),
(8, 'https://images.unsplash.com/photo-1518455027359-f3f8164ba6bd?w=800&q=80'),
(9, 'https://images.unsplash.com/photo-1598550476439-6847785fcea6?w=800&q=80'),
-- Audio
(10, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&q=80'),
(11, 'https://images.unsplash.com/photo-1590602847861-f357a9332bbc?w=800&q=80'),
(12, 'https://images.unsplash.com/photo-1545454675-3531b543be5d?w=800&q=80'),
-- Laptops
(13, 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=800&q=80'),
(14, 'https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=800&q=80'),
(15, 'https://images.unsplash.com/photo-1593642702821-c823b281625c?w=800&q=80'),
-- Accesorios
(16, 'https://images.unsplash.com/photo-1625723044792-44de16ccb4e9?w=800&q=80'),
(17, 'https://images.unsplash.com/photo-1593642532744-d377ab507dc8?w=800&q=80'),
(18, 'https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=800&q=80');


-- ========================================================================
-- V9__add_realistic_products.sql
-- Inserción de productos realistas para las categorías existentes
-- Compatible con schema post-V6 (columnas: destacado, oferta, nuevo en productos;
-- es_principal, orden, fecha_creacion en producto_imagenes)
-- ========================================================================

-- ===================== PERIFÉRICOS =====================
WITH p1 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'Logitech MX Master 3S',
        'Ratón inalámbrico avanzado con sensor óptico 8K DPI. Diseño ergonómico de primera clase ideal para productividad y creación de contenido. Clics silenciosos y rueda de desplazamiento MagSpeed ultrarrápida.',
        99.99, 150, 'Periféricos', 4.8, 1205, 3400, 'ACTIVO', true, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p1;

WITH p2 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'Keychron Q1 Pro',
        'Teclado mecánico custom inalámbrico con chasis de aluminio CNC completo. Diseño al 75% con conectividad Bluetooth 5.1 y cable USB-C. Switches intercambiables en caliente con soporte nativo para QMK/VIA.',
        199.00, 75, 'Periféricos', 4.9, 340, 850, 'ACTIVO', true, false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1595225476474-87563907a212?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p2;

WITH p3 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'Sony WH-1000XM5',
        'Auriculares inalámbricos con cancelación de ruido activa líder en la industria. Audio de alta resolución, hasta 30 horas de autonomía y control táctil inteligente. Perfectos para trabajo y viajes.',
        348.00, 200, 'Periféricos', 4.7, 8900, 15000, 'ACTIVO', true, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p3;

WITH p4 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'Elgato Stream Deck MK.2',
        'Controlador de estudio con 15 teclas LCD personalizables. Automatiza y controla tus aplicaciones, herramientas y plataformas favoritas como OBS, Twitch y YouTube con un solo toque.',
        149.99, 120, 'Periféricos', 4.8, 1150, 4200, 'ACTIVO', false, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1601784551446-20c9e07cdbdb?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p4;

-- ===================== MONITORES =====================
WITH p5 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'LG UltraGear 27" 144Hz Nano IPS',
        'Monitor gaming QHD (2560×1440) de 27 pulgadas con panel Nano IPS. Tiempo de respuesta 1ms (GtG), compatibilidad con NVIDIA G-SYNC y AMD FreeSync Premium. Soporte ajustable en altura e inclinación.',
        299.99, 85, 'Monitores', 4.6, 600, 1200, 'ACTIVO', true, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p5;

WITH p6 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'Dell UltraSharp 32" 4K USB-C Hub',
        'Monitor profesional 4K UHD 31.5 pulgadas con hub USB-C de 90W, Ethernet integrada y cobertura de color de grado profesional (99% sRGB, 99% Rec 709). Ideal para diseño y productividad.',
        759.00, 40, 'Monitores', 4.8, 250, 600, 'ACTIVO', true, false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1586772002130-b0f3daa6288b?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p6;

WITH p7 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'Samsung Odyssey G9 49" Curvo DQHD',
        'Monitor ultrapanorámico 49 pulgadas, curva 1000R, resolución DQHD 5120×1440. Tasa de refresco 240Hz, QLED y panel VA con colores vibrantes. La experiencia gaming definitiva.',
        1299.99, 15, 'Monitores', 4.5, 410, 800, 'ACTIVO', false, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1616012480717-fd9867059ca0?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p7;

WITH p8 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'BenQ Zowie XL2546K 240Hz eSports',
        'Monitor de eSports 1080p con tecnología DyAc+ que minimiza el desenfoque en movimiento. Base rediseñada para optimizar el espacio del escritorio. Control rápido con S-Switch incluido.',
        429.00, 60, 'Monitores', 4.7, 820, 2100, 'ACTIVO', false, false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1593640408182-31c228b60d3d?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p8;

-- ===================== MUEBLES =====================
WITH p9 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'Herman Miller Aeron Remastered',
        'Silla ergonómica de culto con malla Pellicle transpirable que adapta el cuerpo perfectamente. Soporte lumbar PostureFit SL y reposabrazos 4D totalmente ajustables. El estándar de oro en ergonomía de oficina.',
        1450.00, 30, 'Muebles', 4.9, 1400, 3500, 'ACTIVO', true, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1505843490538-5133c6c7d0e1?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p9;

WITH p10 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'Secretlab Titan Evo 2022',
        'Silla gamer premium con espuma precurvada de alta densidad y piel sintética híbrida ultra resistente. Sistema de soporte lumbar magnético L-ADAPT para largas sesiones de trabajo y gaming.',
        549.00, 100, 'Muebles', 4.6, 2100, 6500, 'ACTIVO', true, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1598300042247-d088f8ab3a91?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p10;

WITH p11 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'Flexispot E7 Standing Desk',
        'Escritorio elevable eléctrico con doble motor silencioso y panel táctil con memoria de 4 alturas. Tablero de bambú ecológico resistente a arañazos. Soporta hasta 125kg. Mejora tu postura y salud.',
        499.99, 45, 'Muebles', 4.8, 620, 1300, 'ACTIVO', false, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1518455027359-f3f8164ba6bd?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p11;

WITH p12 AS (
    INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, destacado, oferta, nuevo, fecha_creacion, fecha_actualizacion)
    VALUES (
        'IKEA Alex Cajonera Blanco Mate',
        'Cajonera icónica con 5 cajones espaciosos en blanco mate brillante. El favorito de setuppers y creadores de contenido. Perfecta como elemento independiente o apoyo para encimeras largas de escritorio.',
        89.99, 250, 'Muebles', 4.7, 5400, 22000, 'ACTIVO', false, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
    ) RETURNING id
)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, fecha_creacion)
SELECT id, 'https://images.unsplash.com/photo-1556909172-54557c7e4fb7?w=800&q=80', true, 1, CURRENT_TIMESTAMP FROM p12;


-- ============================================
-- SmartLogix - Inventory Service
-- Migration: V17__add_more_products.sql
-- Description: Add more real products to catalog
-- ============================================

INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, fecha_creacion, fecha_actualizacion, version) VALUES 
-- Periféricos (1 already exists: Teclado Mecánico RGB Keychron, add 2 more)
('Mouse Logitech G Pro X Superlight', 'Mouse inalámbrico ultraligero para eSports.', 130000, 30, 'Periféricos', 4.9, 320, 1500, 'ACTIVO', NOW(), NOW(), 0),
('Webcam Logitech Brio 4K', 'Cámara web Ultra HD 4K con HDR y compatibilidad con Windows Hello.', 199990, 20, 'Periféricos', 4.7, 100, 500, 'ACTIVO', NOW(), NOW(), 0),

-- Monitores (1 already exists: Monitor LG UltraGear 27, add 2 more)
('Monitor Samsung Odyssey G7', 'Monitor curvo gaming 1440p, 240Hz, 1ms.', 550000, 5, 'Monitores', 4.8, 80, 200, 'ACTIVO', NOW(), NOW(), 0),
('Monitor Dell UltraSharp 27', 'Monitor 4K USB-C ideal para productividad y diseño gráfico.', 450000, 12, 'Monitores', 4.6, 95, 300, 'ACTIVO', NOW(), NOW(), 0),

-- Muebles (1 already exists: Silla Ergonómica Pro Office, add 2 more)
('Escritorio Standing Desk Motorizado', 'Escritorio con ajuste de altura eléctrico y memoria de posiciones.', 299990, 8, 'Muebles', 4.8, 60, 120, 'ACTIVO', NOW(), NOW(), 0),
('Silla Gamer Secretlab Titan', 'Silla de cuero sintético premium para largas jornadas de juego.', 350000, 10, 'Muebles', 4.9, 150, 400, 'ACTIVO', NOW(), NOW(), 0),

-- Audio (add 3)
('Audífonos Sony WH-1000XM5', 'Auriculares inalámbricos con cancelación de ruido líder en la industria.', 300000, 25, 'Audio', 4.8, 500, 2000, 'ACTIVO', NOW(), NOW(), 0),
('Micrófono Blue Yeti USB', 'Micrófono condensador para streaming, podcast y grabaciones.', 120000, 40, 'Audio', 4.6, 250, 1000, 'ACTIVO', NOW(), NOW(), 0),
('Altavoces Bose Companion 2', 'Sistema de altavoces multimedia Series III.', 110000, 15, 'Audio', 4.5, 120, 350, 'ACTIVO', NOW(), NOW(), 0),

-- Laptops (add 3)
('MacBook Pro M3 Pro 14"', 'Laptop de 14 pulgadas con chip M3 Pro, 18GB RAM, 512GB SSD.', 1850000, 8, 'Laptops', 4.9, 300, 800, 'ACTIVO', NOW(), NOW(), 0),
('ASUS ROG Zephyrus G14', 'Laptop gaming ultradelgada, Ryzen 9, RTX 4060.', 1500000, 6, 'Laptops', 4.7, 180, 400, 'ACTIVO', NOW(), NOW(), 0),
('Dell XPS 15', 'Laptop premium de 15.6 pulgadas, Core i7, 16GB RAM, RTX 4050.', 1650000, 7, 'Laptops', 4.6, 110, 250, 'ACTIVO', NOW(), NOW(), 0),

-- Accesorios (add 3)
('Hub USB-C Anker 8-in-1', 'Adaptador multipuerto con HDMI 4K, lector SD, Ethernet.', 45000, 50, 'Accesorios', 4.7, 400, 3000, 'ACTIVO', NOW(), NOW(), 0),
('Soporte para Monitor Doble VIVO', 'Soporte articulado de escritorio para dos monitores.', 35000, 35, 'Accesorios', 4.8, 280, 1200, 'ACTIVO', NOW(), NOW(), 0),
('Funda para Laptop Incase', 'Funda protectora acolchada para laptops de 13 y 14 pulgadas.', 25000, 100, 'Accesorios', 4.5, 90, 800, 'ACTIVO', NOW(), NOW(), 0)
ON CONFLICT (nombre) DO NOTHING;

-- INSERT IMAGES

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1593640408182-31c70c8268f5?w=800&q=80' FROM productos WHERE nombre = 'Mouse Logitech G Pro X Superlight' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1588610531238-d621b1f9b369?w=800&q=80' FROM productos WHERE nombre = 'Webcam Logitech Brio 4K' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1527443195645-1133f7f28990?w=800&q=80' FROM productos WHERE nombre = 'Monitor Samsung Odyssey G7' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1517055748807-68b3f46f481e?w=800&q=80' FROM productos WHERE nombre = 'Monitor Dell UltraSharp 27' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1595514535316-8a0328b9fb6c?w=800&q=80' FROM productos WHERE nombre = 'Escritorio Standing Desk Motorizado' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1598550476439-6847785fcea6?w=800&q=80' FROM productos WHERE nombre = 'Silla Gamer Secretlab Titan' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1618366712010-f4ae9c647dcb?w=800&q=80' FROM productos WHERE nombre = 'Audífonos Sony WH-1000XM5' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1590602847861-f357a9332bbc?w=800&q=80' FROM productos WHERE nombre = 'Micrófono Blue Yeti USB' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1545454675-3531b543be5d?w=800&q=80' FROM productos WHERE nombre = 'Altavoces Bose Companion 2' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=800&q=80' FROM productos WHERE nombre = 'MacBook Pro M3 Pro 14"' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1593642632823-8f785ba67e45?w=800&q=80' FROM productos WHERE nombre = 'ASUS ROG Zephyrus G14' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1593642702821-c823b281625c?w=800&q=80' FROM productos WHERE nombre = 'Dell XPS 15' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1605655760927-4a81baec4cc5?w=800&q=80' FROM productos WHERE nombre = 'Hub USB-C Anker 8-in-1' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1593642532744-d377ab507dc8?w=800&q=80' FROM productos WHERE nombre = 'Soporte para Monitor Doble VIVO' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1563810842013-3392ea180a3a?w=800&q=80' FROM productos WHERE nombre = 'Funda para Laptop Incase' ON CONFLICT DO NOTHING;




