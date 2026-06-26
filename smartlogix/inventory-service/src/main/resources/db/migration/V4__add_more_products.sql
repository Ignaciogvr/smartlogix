-- ============================================
-- SmartLogix - Inventory Service
-- Migration: V4__add_more_products.sql
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

