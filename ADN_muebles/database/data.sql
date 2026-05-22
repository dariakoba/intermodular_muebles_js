START TRANSACTION;

INSERT INTO `categoria` (`id_categoria`, `nombre`) VALUES
(1, 'Sofás'),
(2, 'Mesas'),
(3, 'Sillas'),
(4, 'Camas'),
(5, 'Armarios'),
(6, 'Estanterías'),
(7, 'Muebles TV'),
(8, 'Escritorios');

INSERT INTO `linea_pedido` (`id`, `id_pedido`, `id_ejemplar`) VALUES
(1, 1, 1),
(2, 2, 3),
(3, 3, 5),
(4, 4, 6),
(5, 5, 9);

INSERT INTO `pedidos` (`id`, `fecha_pedido`, `fecha_devolucion`, `precio`, `metodo_pago`, `factura`, `envio`, `id_usuario`) VALUES
(1, '2024-03-01', NULL, 550.00, 'Tarjeta', 'F001', 'DHL', 2),
(2, '2024-03-02', NULL, 300.00, 'PayPal', 'F002', 'MRW', 3),
(3, '2024-03-03', NULL, 60.00, 'Tarjeta', 'F003', 'SEUR', 4),
(4, '2024-03-04', NULL, 450.00, 'Tarjeta', 'F004', 'DHL', 5),
(5, '2024-03-05', NULL, 200.00, 'PayPal', 'F005', 'MRW', 2);

INSERT INTO `productos` (`id_producto`, `nombre`, `color`, `precio`, `stock`, `descripcion`, `categoria_id`) VALUES
(1, 'Sofá 3 plazas', 'Gris', 550.00, 5, 'Sofá cómodo de 3 plazas', 1),
(2, 'Sofá chaise longue', 'Beige', 899.00, 2, 'Sofá grande con chaise longue', 1),
(3, 'Mesa comedor', 'Madera', 300.00, 10, 'Mesa de comedor para 6 personas', 2),
(4, 'Mesa centro', 'Blanco', 120.00, 8, 'Mesa de centro moderna', 2),
(5, 'Silla comedor', 'Negro', 60.00, 20, 'Silla acolchada', 3),
(6, 'Cama matrimonio', 'Blanco', 450.00, 4, 'Cama de 135cm', 4),
(7, 'Armario grande', 'Roble', 700.00, 3, 'Armario 3 puertas', 5),
(8, 'Estantería', 'Blanco', 150.00, 6, 'Estantería moderna', 6),
(9, 'Mueble TV', 'Marrón', 200.00, 7, 'Mueble para televisión', 7),
(10, 'Escritorio', 'Roble', 250.00, 5, 'Escritorio oficina', 8);

INSERT INTO `usuarios` (`id`, `password_hash`, `rol`, `telefono`, `estado`, `nombre`, `apellidos`, `direccion`, `email`, `nivel_acceso`, `puntos`, `salario`, `fecha_alta`) VALUES
(1, 'hash1', 'admin', '600111111', 'activo', 'Admin', 'Principal', 'Calle Admin 1', 'admin@dna.com', NULL, NULL, NULL, '2024-01-01'),
(2, 'hash2', 'cliente', '600222222', 'activo', 'Laura', 'García', 'Calle Sofa 12', 'laura@gmail.com', NULL, 120, NULL, '2024-02-01'),
(3, 'hash3', 'cliente', '600333333', 'activo', 'Carlos', 'Pérez', 'Av Mesa 45', 'carlos@gmail.com', NULL, 60, NULL, '2024-02-10'),
(4, 'hash4', 'cliente', '600444444', 'activo', 'Marta', 'López', 'Calle Silla 7', 'marta@gmail.com', NULL, 30, NULL, '2024-03-05'),
(5, 'hash5', 'cliente', '600555555', 'activo', 'David', 'Ruiz', 'Av Mueble 22', 'david@gmail.com', NULL, 80, NULL, '2024-03-15'),
(6, '$2a$10$8/ix4vptyLUBhVz.Ybh/5.6lPXHu/bSl8wT5VGmn0asL99TDj1qju', 'admin', '632439380', 'activo', 'adminNoelia', 'uwu', '', 'noelia@gmail.com', 0, 0, 0.00, '2026-03-25'),
(7, '$2a$10$RCe03ppx23Z.EG2tsumteOEWSl4yya3D7uRVf3w5jhnUrAMr5uTs2', 'cliente', '5444455', 'activo', 'test', 'test', '', 'test@gmail.com', NULL, 0, 0.00, '2026-03-27'),
(8, '$2a$10$IMh9mt6J9oCXaGv.fPMTtuFRI82u6E/Gd5A1fA1FtnDNaLKIz55nq', 'cliente', '123321', 'activo', 'test2', 'test2', 'atet2', 'test2@gmail.com', NULL, 0, 0.00, '2026-03-27'),
(9, '$2a$10$9Z/lvh0uyvz0.H/V3xMIy.icMGxC7Amm.8FqL4zMiABMegoemLHmy', 'cliente', '234432', 'activo', 't3', 't3', 't3', 't3@gmail.com', NULL, 0, 0.00, '2026-03-27');

COMMIT;