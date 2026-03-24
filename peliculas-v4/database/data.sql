

-- =====================================
-- INSERT USUARIOS
-- =====================================
INSERT INTO usuarios (password_hash, rol, telefono, estado, nombre, apellidos, direccion, email, nivel_acceso, puntos, salario, fecha_alta)
VALUES
('hash1','admin','600111111','activo','Admin','Principal','Calle Admin 1','admin@dna.com',NULL,NULL,NULL,'2024-01-01'),
('hash2','cliente','600222222','activo','Laura','García','Calle Sofa 12','laura@gmail.com',NULL,120,NULL,'2024-02-01'),
('hash3','cliente','600333333','activo','Carlos','Pérez','Av Mesa 45','carlos@gmail.com',NULL,60,NULL,'2024-02-10'),
('hash4','cliente','600444444','activo','Marta','López','Calle Silla 7','marta@gmail.com',NULL,30,NULL,'2024-03-05'),
('hash5','cliente','600555555','activo','David','Ruiz','Av Mueble 22','david@gmail.com',NULL,80,NULL,'2024-03-15');

-- =====================================
-- INSERT CATEGORIAS (MUEBLES)
-- =====================================
INSERT INTO categoria (nombre) VALUES
('Sofás'),
('Mesas'),
('Sillas'),
('Camas'),
('Armarios'),
('Estanterías'),
('Muebles TV'),
('Escritorios');

-- =====================================
-- INSERT PRODUCTOS
-- =====================================
INSERT INTO productos (nombre, color, precio, stock, descripcion, id_categoria) VALUES
('Sofá 3 plazas', 'Gris', 550, 5, 'Sofá cómodo de 3 plazas', 1),
('Sofá chaise longue', 'Beige', 899, 2, 'Sofá grande con chaise longue', 1),
('Mesa comedor', 'Madera', 300, 10, 'Mesa de comedor para 6 personas', 2),
('Mesa centro', 'Blanco', 120, 8, 'Mesa de centro moderna', 2),
('Silla comedor', 'Negro', 60, 20, 'Silla acolchada', 3),
('Cama matrimonio', 'Blanco', 450, 4, 'Cama de 135cm', 4),
('Armario grande', 'Roble', 700, 3, 'Armario 3 puertas', 5),
('Estantería', 'Blanco', 150, 6, 'Estantería moderna', 6),
('Mueble TV', 'Marrón', 200, 7, 'Mueble para televisión', 7),
('Escritorio', 'Roble', 250, 5, 'Escritorio oficina', 8);

-- =====================================
-- INSERT EJEMPLAR
-- =====================================
INSERT INTO ejemplar (estado, id_producto) VALUES
('nuevo',1),
('nuevo',2),
('nuevo',3),
('nuevo',4),
('nuevo',5),
('nuevo',6),
('nuevo',7),
('nuevo',8),
('nuevo',9),
('nuevo',10);

-- =====================================
-- INSERT PEDIDOS
-- =====================================
INSERT INTO pedidos (fecha_pedido, fecha_devolucion, precio, metodo_pago, factura, envio, id_usuario) VALUES
('2024-03-01', NULL, 550, 'Tarjeta', 'F001', 'DHL', 2),
('2024-03-02', NULL, 300, 'PayPal', 'F002', 'MRW', 3),
('2024-03-03', NULL, 60, 'Tarjeta', 'F003', 'SEUR', 4),
('2024-03-04', NULL, 450, 'Tarjeta', 'F004', 'DHL', 5),
('2024-03-05', NULL, 200, 'PayPal', 'F005', 'MRW', 2);

-- =====================================
-- INSERT LINEA_PEDIDO
-- =====================================
INSERT INTO linea_pedido (id_pedido, id_ejemplar) VALUES
(1,1),
(2,3),
(3,5),
(4,6),
(5,9);

-- =====================================
-- SELECT PARA TU API /api/productos
-- =====================================
SELECT 
    p.id,
    p.nombre,
    p.descripcion,
    p.precio,
    p.stock,
    p.color,
    c.nombre AS categoria
FROM productos p
JOIN categoria c ON p.id_categoria = c.id;

-- =====================================
-- INSERT RESENYAS
-- =====================================
INSERT INTO resenyas (comentario, puntuacion, fecha_publicacion, id_usuario, id_producto) VALUES
('El sofá es súper cómodo y el color gris queda genial en mi salón.', 5, '2024-03-10', 2, 1),
('La mesa de comedor llegó con un pequeño rasguño, pero es robusta.', 3, '2024-03-12', 3, 3),
('La silla es básica pero cumple su función por el precio que tiene.', 4, '2024-03-15', 4, 5),
('Cama de muy buena calidad, el montaje fue sencillo.', 5, '2024-03-20', 5, 6),
('El mueble de TV es un poco más oscuro de lo que se ve en la foto.', 4, '2024-03-22', 2, 9),
('Me encanta el diseño de la mesa de centro, muy minimalista.', 5, '2024-03-25', 3, 4);

-- =====================================
-- SELECT PARA COMPROBAR RESEÑAS CON NOMBRES
-- =====================================
SELECT 
    r.id_resenya,
    u.nombre AS cliente,
    p.nombre AS producto,
    r.puntuacion,
    r.comentario,
    r.fecha_publicacion
FROM resenyas r
JOIN usuarios u ON r.id_usuario = u.id
JOIN productos p ON r.id_producto = p.id_producto;