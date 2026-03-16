-- =====================================
-- INSERT USUARIOS
-- =====================================

INSERT INTO usuarios (password_hash, rol, telefono, estado, nombre, apellidos, direccion, email) VALUES
('hash1','admin','600111111','activo','Carlos','Gomez','Calle Mayor 10','carlos@tienda.com'),
('hash2','empleado','600222222','activo','Ana','Lopez','Calle Sol 12','ana@tienda.com'),
('hash3','empleado','600333333','activo','Luis','Perez','Calle Luna 5','luis@tienda.com'),
('hash4','cliente','600444444','activo','Maria','Garcia','Av. Valencia 20','maria@gmail.com'),
('hash5','cliente','600555555','activo','Pedro','Martinez','Calle Norte 8','pedro@gmail.com'),
('hash6','cliente','600666666','activo','Lucia','Sanchez','Calle Sur 15','lucia@gmail.com');

-- =====================================
-- INSERT ADMIN
-- =====================================

INSERT INTO admin (id, nivel_de_acceso) VALUES
(1, 10);

-- =====================================
-- INSERT EMPLEADOS
-- =====================================

INSERT INTO empleados (id, fecha_contratacion, salario) VALUES
(2, '2023-02-10', 1500.00),
(3, '2024-01-15', 1400.00);

-- =====================================
-- INSERT CLIENTES
-- =====================================

INSERT INTO clientes (id, fecha_alta, puntos) VALUES
(4, '2024-05-01', 100),
(5, '2024-06-10', 50),
(6, '2024-07-20', 200);

-- =====================================
-- INSERT CATEGORIAS
-- =====================================

INSERT INTO categoria (nombre) VALUES
('Sofas'),
('Mesas'),
('Sillas'),
('Camas'),
('Armarios');

-- =====================================
-- INSERT PRODUCTOS
-- =====================================

INSERT INTO productos (nombre, color, precio, stock, descripcion, id_categoria) VALUES
('Sofa Moderno','Gris',450.00,10,'Sofa de tres plazas',1),
('Mesa Comedor','Madera',300.00,8,'Mesa grande de comedor',2),
('Silla Oficina','Negro',120.00,20,'Silla ergonomica',3),
('Cama Matrimonio','Blanco',600.00,5,'Cama 150x200',4),
('Armario Grande','Roble',800.00,3,'Armario de 3 puertas',5);

-- =====================================
-- INSERT EJEMPLARES
-- =====================================

INSERT INTO ejemplar (estado, id_producto) VALUES
('nuevo',1),
('nuevo',1),
('nuevo',2),
('nuevo',2),
('nuevo',3),
('nuevo',3),
('nuevo',4),
('nuevo',5);

-- =====================================
-- INSERT PEDIDOS
-- =====================================

INSERT INTO pedidos (fecha_pedido, fecha_devolucion, precio, metodo_pago, factura, envio, id_cliente) VALUES
('2025-01-10', NULL, 450.00, 'tarjeta', 'F001', 'domicilio', 4),
('2025-01-15', NULL, 300.00, 'paypal', 'F002', 'recogida', 5),
('2025-02-01', NULL, 120.00, 'tarjeta', 'F003', 'domicilio', 6);

-- =====================================
-- INSERT LINEA_PEDIDO
-- =====================================

INSERT INTO linea_pedido (id_pedido, id_ejemplar) VALUES
(1,1),
(2,3),
(3,5);