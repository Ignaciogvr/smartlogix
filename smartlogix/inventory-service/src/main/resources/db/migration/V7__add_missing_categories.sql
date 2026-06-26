-- ============================================
-- SmartLogix - Inventory Service
-- Migration: V7__add_missing_categories.sql
-- Description: Add products for missing categories: Redes, Gaming, Almacenamiento, Smart Home
-- ============================================

INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, fecha_creacion, fecha_actualizacion, version) VALUES 
-- Redes
('Switch Cisco Catalyst 2960', 'Switch gestionable de 24 puertos Gigabit para redes empresariales.', 450000, 15, 'Redes', 4.7, 80, 200, 'ACTIVO', NOW(), NOW(), 0),
('Cable Ethernet Cat6 UTP', 'Cable de red Cat6 de 10 metros, blindado para alta velocidad.', 8990, 200, 'Redes', 4.5, 300, 1500, 'ACTIVO', NOW(), NOW(), 0),
('Access Point Ubiquiti UniFi', 'Punto de acceso Wi-Fi de alto rendimiento para interiores.', 120000, 25, 'Redes', 4.8, 150, 400, 'ACTIVO', NOW(), NOW(), 0),

-- Gaming
('Consola PlayStation 5', 'Consola de última generación con lector de discos 4K.', 650000, 10, 'Gaming', 4.9, 500, 2000, 'ACTIVO', NOW(), NOW(), 0),
('Joystick Xbox Elite Series 2', 'Control premium con paddles traseros programables.', 120000, 30, 'Gaming', 4.8, 250, 800, 'ACTIVO', NOW(), NOW(), 0),
('Tarjeta Gráfica RTX 4070', 'GPU NVIDIA GeForce RTX 4070 12GB GDDR6X.', 850000, 5, 'Gaming', 4.9, 100, 300, 'ACTIVO', NOW(), NOW(), 0),

-- Almacenamiento
('SSD Samsung 980 Pro 1TB', 'Unidad de estado sólido NVMe M.2 de alta velocidad.', 180000, 40, 'Almacenamiento', 4.8, 400, 1200, 'ACTIVO', NOW(), NOW(), 0),
('Disco Duro Externo 4TB', 'Disco externo portátil USB 3.0 de 4TB.', 120000, 35, 'Almacenamiento', 4.6, 200, 600, 'ACTIVO', NOW(), NOW(), 0),
('Memoria USB 128GB', 'Unidad flash USB 3.1 de alta capacidad.', 25000, 150, 'Almacenamiento', 4.5, 350, 1800, 'ACTIVO', NOW(), NOW(), 0),

-- Smart Home
('Google Nest Hub', 'Pantalla inteligente con Google Assistant y control del hogar.', 180000, 20, 'Smart Home', 4.7, 180, 500, 'ACTIVO', NOW(), NOW(), 0),
('Cámara de Seguridad Ring', 'Cámara Wi-Fi con visión nocturna y detección de movimiento.', 120000, 25, 'Smart Home', 4.6, 220, 700, 'ACTIVO', NOW(), NOW(), 0),
('Termostato Inteligente Ecobee', 'Termostato WiFi con control por voz y ahorro de energía.', 150000, 18, 'Smart Home', 4.8, 150, 400, 'ACTIVO', NOW(), NOW(), 0)
ON CONFLICT (nombre) DO NOTHING;

-- INSERT IMAGES

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1558494949-ef010cbdcc31?w=800&q=80', true, 0 FROM productos WHERE nombre = 'Switch Cisco Catalyst 2960' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1605810230434-7631ac76ec81?w=800&q=80', true, 0 FROM productos WHERE nombre = 'Cable Ethernet Cat6 UTP' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=800&q=80', true, 0 FROM productos WHERE nombre = 'Access Point Ubiquiti UniFi' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1606144042614-b2417e99c4e3?w=800&q=80', true, 0 FROM productos WHERE nombre = 'Consola PlayStation 5' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1592840496694-26d035b52b48?w=800&q=80', true, 0 FROM productos WHERE nombre = 'Joystick Xbox Elite Series 2' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1591488320449-011701bb6704?w=800&q=80', true, 0 FROM productos WHERE nombre = 'Tarjeta Gráfica RTX 4070' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1597872200969-2b65d56bd16b?w=800&q=80', true, 0 FROM productos WHERE nombre = 'SSD Samsung 980 Pro 1TB' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1531492746076-161ca9bcad58?w=800&q=80', true, 0 FROM productos WHERE nombre = 'Disco Duro Externo 4TB' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1618410320928-25228d811631?w=800&q=80', true, 0 FROM productos WHERE nombre = 'Memoria USB 128GB' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1558002038-1091a1661116?w=800&q=80', true, 0 FROM productos WHERE nombre = 'Google Nest Hub' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1557597774-9d273605dfa9?w=800&q=80', true, 0 FROM productos WHERE nombre = 'Cámara de Seguridad Ring' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden)
SELECT id, 'https://images.unsplash.com/photo-1563810842013-3392ea180a3a?w=800&q=80', true, 0 FROM productos WHERE nombre = 'Termostato Inteligente Ecobee' ON CONFLICT DO NOTHING;
