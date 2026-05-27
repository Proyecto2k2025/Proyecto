-- CLIENTES
INSERT INTO clientes (nombre, email, telefono, direccion)
VALUES 
('Juan Perez', 'juan@gmail.com', '611111111', 'Calle Mayor 10'),
('Ana Lopez', 'ana@gmail.com', '622222222', 'Avenida Sol 25'),
('Carlos Ruiz', 'carlos@gmail.com', '633333333', 'Plaza España 5');


-- PRODUCTOS
INSERT INTO productos (nombre, descripcion, precio, categoria, material)
VALUES
('Alfombra Zoro', 'Alfombra anime de Zoro', 39.99, 'Anime', 'Poliester'),
('Alfombra Luffy', 'Alfombra One Piece Luffy', 42.50, 'Anime', 'Algodon'),
('Alfombra Naruto', 'Alfombra Naruto Shippuden', 35.99, 'Anime', 'Poliester');


-- VARIANTES
INSERT INTO variantes (talla, color, stock_minimo, sku, stock, productos_id)
VALUES
('S', 'Verde', 5, 'ZORO-S-01', 20, 1),
('M', 'Verde', 5, 'ZORO-M-01', 15, 1),

('L', 'Rojo', 5, 'LUFFY-L-01', 12, 2),
('XL', 'Rojo', 5, 'LUFFY-XL-01', 8, 2),

('M', 'Naranja', 5, 'NARUTO-M-01', 10, 3);


-- PEDIDOS
INSERT INTO pedidos (estado, total, fecha_pedido, clientes_id)
VALUES
('PENDIENTE', 79.98, '2025-05-27', 1),
('ENVIADO', 42.50, '2025-05-26', 2),
('ENTREGADO', 35.99, '2025-05-25', 3);


-- ITEMS DEL PEDIDO
INSERT INTO pedido_items (cantidad, precio_unitario, pedidos_id, variantes_id)
VALUES
(2, 39.99, 1, 1),
(1, 42.50, 2, 3),
(1, 35.99, 3, 5);


-- USUARIOS
INSERT INTO usuarios (nombre, password, rol, clientes_id)
VALUES
('juanuser', '1234', 'CLIENTE', 1),
('anauser', '1234', 'CLIENTE', 2),
('admin', 'admin123', 'ADMIN', 3);
