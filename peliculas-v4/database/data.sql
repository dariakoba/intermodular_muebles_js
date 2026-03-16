-- =========================
-- TABLA USUARIOS
-- =========================
INSERT INTO usuarios (password_hash, rol, telefono, estado, nombre, apellidos, direccion, email, nivel_acceso, puntos, salario, fecha_alta)
VALUES
('hash1','admin','123456789','activo','Juan','Pérez','Calle Falsa 1','juan.perez@email.com',1,NULL,NULL,'2024-01-10'),
('hash2','cliente','987654321','activo','Ana','Gómez','Av. Siempre Viva 2','ana.gomez@email.com',NULL,150,NULL,'2024-02-15'),
('hash3','cliente','654321987','inactivo','Luis','Ramírez','Calle Luna 3','luis.ramirez@email.com',NULL,200,NULL,'2024-03-20'),
('hash4','empleado','321654987','activo','Marta','Santos','Calle Sol 4','marta.santos@email.com',NULL,NULL,1500.00,'2024-01-05'),
('hash5','empleado','789456123','activo','Carlos','Fernández','Av. Estrella 5','carlos.fernandez@email.com',NULL,NULL,1200.00,'2024-02-10'),
('hash6','cliente','123789456','activo','Lucía','Torres','Calle Mar 6','lucia.torres@email.com',NULL,50,NULL,'2024-03-01'),
('hash7','empleado','456123789','activo','Pedro','Vega','Av. Río 7','pedro.vega@email.com',NULL,NULL,1800.00,'2024-02-25'),
('hash8','cliente','321987654','activo','Sofía','Ríos','Calle Nube 8','sofia.rios@email.com',NULL,100,NULL,'2024-01-30'),
('hash9','admin','147258369','activo','Miguel','Cruz','Av. Sol 9','miguel.cruz@email.com',2,NULL,NULL,'2024-03-05'),
('hash10','cliente','963852741','activo','Elena','Díaz','Calle Estrella 10','elena.diaz@email.com',NULL,75,NULL,'2024-03-10');

-- =========================
-- TABLA CATEGORIA
-- =========================
INSERT INTO categoria (nombre)
VALUES
('Electrónica'),
('Ropa'),
('Hogar'),
('Juguetes'),
('Deportes'),
('Libros'),
('Belleza'),
('Alimentos'),
('Muebles'),
('Accesorios');

-- =========================
-- TABLA PRODUCTOS
-- =========================
INSERT INTO productos (nombre, color, precio, stock, descripcion, id_categoria)
VALUES
('Televisor', 'Negro', 450.00, 10, 'Televisor LED 42 pulgadas', 1),
('Camiseta', 'Azul', 25.50, 50, 'Camiseta de algodón talla M', 2),
('Sofá', 'Gris', 600.00, 5, 'Sofá de 3 plazas', 3),
('Pelota', 'Rojo', 15.00, 30, 'Pelota de fútbol tamaño 5', 4),
('Libro de ciencia', 'N/A', 20.00, 40, 'Libro educativo de ciencias', 6),
('Perfume', 'Transparente', 75.00, 20, 'Perfume de 50ml', 7),
('Galletas', 'N/A', 3.50, 100, 'Paquete de galletas', 8),
('Mesa de comedor', 'Madera', 350.00, 7, 'Mesa de comedor para 6 personas', 9),
('Auriculares', 'Negro', 80.00, 15, 'Auriculares bluetooth', 1),
('Bolso', 'Marrón', 55.00, 25, 'Bolso de cuero', 10);

-- =========================
-- TABLA EJEMPLAR
-- =========================
INSERT INTO ejemplar (estado, id_producto)
VALUES
('nuevo', 1),
('nuevo', 2),
('usado', 3),
('nuevo', 4),
('nuevo', 5),
('usado', 6),
('nuevo', 7),
('nuevo', 8),
('nuevo', 9),
('usado', 10);

-- =========================
-- TABLA PEDIDOS
-- =========================
INSERT INTO pedidos (fecha_pedido, fecha_devolucion, precio, metodo_pago, factura, envio, id_usuario)
VALUES
('2024-03-01', NULL, 450.00, 'Tarjeta', 'F001', 'DHL', 2),
('2024-03-02', NULL, 25.50, 'PayPal', 'F002', 'MRW', 3),
('2024-03-03', NULL, 600.00, 'Tarjeta', 'F003', 'UPS', 6),
('2024-03-04', NULL, 15.00, 'Efectivo', 'F004', 'DHL', 8),
('2024-03-05', NULL, 20.00, 'Tarjeta', 'F005', 'MRW', 2),
('2024-03-06', NULL, 75.00, 'PayPal', 'F006', 'UPS', 3),
('2024-03-07', NULL, 350.00, 'Tarjeta', 'F007', 'DHL', 6),
('2024-03-08', NULL, 80.00, 'Efectivo', 'F008', 'MRW', 8),
('2024-03-09', NULL, 55.00, 'Tarjeta', 'F009', 'UPS', 2),
('2024-03-10', NULL, 3.50, 'PayPal', 'F010', 'DHL', 3);

-- =========================
-- TABLA LINEA_PEDIDO
-- =========================
INSERT INTO linea_pedido (id_pedido, id_ejemplar)
VALUES
(1,1),
(2,2),
(3,3),
(4,4),
(5,5),
(6,6),
(7,7),
(8,8),
(9,9),
(10,10);

