-- Insertar productos con imágenes persistidas desde CDN (Unsplash)
INSERT INTO productos (nombre, descripcion, precio, stock, categoria, rating_promedio, total_ratings, cantidad_vendidos, estado, fecha_creacion, fecha_actualizacion, version) VALUES 
('Teclado Mecánico RGB Keychron', 'Teclado mecánico con switches Gateron Red, retroiluminación RGB, layout 75%.', 85000, 48, 'Periféricos', 4.8, 120, 300, 'ACTIVO', NOW(), NOW(), 0),
('Silla Ergonómica Pro Office', 'Silla de oficina con soporte lumbar ajustable, malla transpirable y reposabrazos 4D.', 150000, 15, 'Muebles', 4.5, 45, 80, 'ACTIVO', NOW(), NOW(), 0),
('Monitor LG UltraGear 27', 'Monitor gaming IPS de 27 pulgadas, 144Hz, 1ms de tiempo de respuesta.', 250000, 10, 'Monitores', 4.9, 200, 450, 'ACTIVO', NOW(), NOW(), 0)
ON CONFLICT (nombre) DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1595225476474-87563907a212?w=800&q=80' FROM productos WHERE nombre = 'Teclado Mecánico RGB Keychron' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1580480055273-228ff5388ef8?w=800&q=80' FROM productos WHERE nombre = 'Silla Ergonómica Pro Office' ON CONFLICT DO NOTHING;

INSERT INTO producto_imagenes (producto_id, imagen_url)
SELECT id, 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=800&q=80' FROM productos WHERE nombre = 'Monitor LG UltraGear 27' ON CONFLICT DO NOTHING;
