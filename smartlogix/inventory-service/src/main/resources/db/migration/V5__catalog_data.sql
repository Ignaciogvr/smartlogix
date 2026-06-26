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
