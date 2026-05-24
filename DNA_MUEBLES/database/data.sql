-- =========================
-- CATEGORIA
-- =========================
INSERT INTO categoria (id_categoria, nombre, deleted_at) VALUES
(1, 'Sofás', '2026-04-26 16:58:43'),
(2, 'Mesas', NULL),
(3, 'Sillas', NULL),
(4, 'Camas', NULL),
(5, 'Armarios', NULL),
(6, 'Estanterías', NULL),
(7, 'Muebles TV', '2026-04-26 16:35:31'),
(8, 'Escritorios', NULL),
(9, 'Ventanas', NULL),
(10, 'test', '2026-04-26 17:12:51'),
(11, 'test2cate', '2026-04-26 17:12:38');

-- =========================
-- USUARIOS
-- =========================
INSERT INTO usuarios 
(id, password_hash, rol, telefono, estado, nombre, apellidos, direccion, email, puntos, salario, fecha_alta) VALUES
(30, '$2a$10$xxx', 'cliente', '123456789', 'activo', 'John', 'Perez', 'Calle Perez 34', 'user4@gmail.com', 5995, 0.00, '2026-04-16'),
(43, '$2a$10$xxx', 'admin', '0681536377', 'activo', 'Raul', 'Cobaltt', 'Calle Plana 87', 'user5@gmail.com', 0, 0.00, '2026-04-22'),
(61, '$2a$10$xxx', 'cliente', '+34123456789', 'activo', 'Daria', 'Koba', 'Calle Pan 34', 'user6@gmail.com', 3728, 0.00, '2026-04-27'),
(62, '$2a$10$xxx', 'cliente', '0681536370', 'activo', 'Laura', 'Rudolfo', 'Zavodska 19', 'user9@gmail.com', 0, 0.00, '2026-04-27'),
(66, '$2a$10$xxx', 'cliente', '123456789', 'activo', 'Pepe', 'Maria', 'Calle Santa Barbara', 'user7@gmail.com', 12850, 0.00, '2026-05-19'),
(68, '$2a$10$xxx', 'cliente', '123456789', 'inactivo', 'Maria', 'Lopez', 'Zavodska 19', 'user8@gmail.com', 0, 0.00, '2026-05-21'),
(69, '$2a$10$xxx', 'cliente', '123456789', 'activo', 'Max', 'García', 'Zavodska 19', 'dashaka7@gmail.com', 0, 0.00, '2026-05-22'),
(74, '$2a$10$xxx', 'cliente', '123456789', 'inactivo', 'Pablo', 'Rei', 'greg 78', 'us7@gmail.com', 4495, 0.00, '2026-05-22'),
(75, '$2a$10$xxx', 'cliente', '123456789', 'activo', 'Sherlock', 'Holmes', '', 'dasha4g@gmail.com', 0, 0.00, '2026-05-23'),
(76, '$2a$10$xxx', 'cliente', '+34589687453', 'activo', 'John', 'Watson', '', 'fewfba7@gmail.com', 0, 0.00, '2026-05-23'),
(78, '$2a$10$xxx', 'cliente', '0681536370', 'activo', 'Amelia', 'Gonzales', '', 'dasgreg@gmail.com', 0, 0.00, '2026-05-23');

-- =========================
-- PRODUCTOS
-- =========================
INSERT INTO productos 
(id_producto, nombre, color, precio, stock, descripcion, categoria_id, deleted_at) VALUES
(1, 'Sofá 4 plazas', 'Gris', 5503.00, 55332, 'Sofá cómodo', 1, NULL),
(2, 'Sofá chaise longue', 'Gris', 899.00, 0, 'Sofá grande', 1, NULL),
(3, 'Mesa comedor', 'Negro', 300.00, 10, 'Mesa 6 personas', 2, NULL),
(4, 'Mesa centro', 'Blanco', 120.00, 8, 'Mesa moderna', 2, NULL),
(5, 'Silla comedor', 'Negro', 60.00, 20, 'Silla acolchada', 3, NULL),
(6, 'Cama matrimonio', 'Beige', 450.00, 4, 'Cama 135cm', 4, NULL),
(7, 'Armario grande', 'Blanco', 700.00, 3, 'Armario 3 puertas', 5, NULL),
(8, 'Estantería', 'Blanco', 150.00, 6, 'Estantería moderna', 6, NULL),
(9, 'Mueble TV', 'Beige', 200.00, 7, 'Mueble TV', 7, NULL),
(10, 'Escritorio', 'Blanco', 250.00, 5, 'Escritorio oficina', 8, NULL);

-- =========================
-- PRODUCTO IMAGENES
-- =========================
INSERT INTO producto_imagenes (id, producto_id, url) VALUES
(3, 1, '/uploads/productos/1.jpg'),
(9, 1, '/uploads/productos/2.jpg'),
(10, 2, '/uploads/productos/3.jpg'),
(11, 3, '/uploads/productos/4.jpg'),
(12, 4, '/uploads/productos/5.jpg'),
(14, 6, '/uploads/productos/6.jpg'),
(15, 7, '/uploads/productos/7.jpg'),
(16, 8, '/uploads/productos/8.jpg'),
(17, 5, '/uploads/productos/9.jpg'),
(18, 9, '/uploads/productos/10.jpg'),
(19, 10, '/uploads/productos/11.jpg');

-- =========================
-- PEDIDOS
-- SOLO "Recibido" si quieres filtrar reseñas
-- =========================
INSERT INTO pedidos 
(id_pedido, id_usuario, fecha, cliente_nombre, total, metodo_pago, estado_pago, activo, puntos_usados) VALUES
(6, 75, '2026-05-22 18:45:00', 'Sherlock Holmes', 150.00, 'Tarjeta', 'Cancelado', 1, 0),
(8, 78, '2026-05-23 00:00:00', 'Amelia Gonzales', 250.00, 'Transferencia', 'Recibido', 1, 0),
(14, 30, '2026-05-24 00:00:00', 'John Perez', 899.00, 'Transferencia', 'Pendiente de pago', 1, 0),
(15, 61, '2026-05-24 00:00:00', 'Daria Koba', 737.70, 'Transferencia', 'Recibido', 1, 2230),
(16, 30, '2026-05-24 00:00:00', 'John Perez', 300.00, 'Transferencia', 'Pendiente de pago', 1, 0),
(17, 66, '2026-05-24 00:00:00', 'Pepe Maria', 2558.00, 'Transferencia', 'Recibido', 1, 0);

-- =========================
-- DETALLES PEDIDOS
-- =========================
INSERT INTO detalles_pedidos (id, id_pedido, id_producto, cantidad, precio_unitario) VALUES
(7, 6, 8, 1, 150.00),
(9, 8, 10, 1, 250.00),
(12, 14, 2, 1, 899.00),
(13, 15, 7, 1, 700.00),
(14, 15, 5, 1, 60.00),
(15, 16, 3, 1, 300.00),
(16, 17, 7, 1, 700.00),
(17, 17, 5, 1, 60.00),
(18, 17, 2, 2, 899.00);

-- =========================
-- RESENAS
-- =========================
INSERT INTO resenas 
(id_resena, id_producto, id_usuario, puntuacion, comentario, fecha) VALUES
(77, 8, 61, 5, 'Perfecta', '2026-05-24 13:46:27'),
(78, 5, 61, 5, 'Me encantó', '2026-05-24 16:01:19'),
(79, 7, 61, 3, 'Buena', '2026-05-24 16:02:00'),
(80, 5, 66, 4, 'Muy buena', '2026-05-24 16:02:34'),
(81, 7, 66, 5, 'Excelente', '2026-05-24 16:03:35'),
(82, 2, 66, 5, 'Muy buena', '2026-05-24 16:04:10');

-- =========================
-- RESENA IMAGENES
-- =========================
INSERT INTO resena_imagenes (id, resena_id, url) VALUES
(32, 78, '/uploads/resenyas/img1.webp'),
(33, 78, '/uploads/resenyas/img2.jfif'),
(34, 80, '/uploads/resenyas/img3.jfif'),
(35, 81, '/uploads/resenyas/img4.jfif');