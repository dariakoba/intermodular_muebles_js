START TRANSACTION;

INSERT INTO categoria (id_categoria, nombre, deleted_at) VALUES
(1, 'Sofás', NULL),
(2, 'Mesas', NULL),
(3, 'Sillas', NULL),
(4, 'Camas', NULL),
(5, 'Armarios', NULL),
(6, 'Estanterías', NULL),
(7, 'Muebles TV', NULL),
(8, 'Escritorios', NULL);

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

INSERT INTO productos (id_producto, nombre, color, precio, stock, descripcion, categoria_id, deleted_at) VALUES
(1, 'Sofá 4 plazas', 'Gris', 5503.00, 55332, 'Sofá cómodo de 3 plazas.update', 1, NULL),
(2, 'Sofá chaise longue', 'Gris', 899.00, 2, 'Sofá grande con chaise longue', 1, NULL),
(3, 'Mesa comedor', 'Negro', 300.00, 10, 'Mesa de comedor para 6 personas', 2, NULL),
(4, 'Mesa centro', 'Blanco', 120.00, 8, 'Mesa de centro moderna', 2, NULL),
(5, 'Silla comedor', 'Negro', 60.00, 20, 'Silla acolchada', 3, NULL),
(6, 'Cama matrimonio', 'Beige', 450.00, 4, 'Cama de 135cm', 4, NULL),
(7, 'Armario grande', 'Blanco', 700.00, 3, 'Armario 3 puertas', 5, NULL),
(8, 'Estantería', 'Blanco', 150.00, 6, 'Estantería moderna', 6, NULL),
(9, 'Mueble TV', 'Beige', 200.00, 7, 'Mueble para televisión', 7, NULL),
(10, 'Escritorio', 'Blanco', 250.00, 5, 'Escritorio oficina', 8, NULL);

INSERT INTO producto_imagenes (id, producto_id, url) VALUES
(3, 1, '/uploads/productos/bc926914-b85e-45ee-a347-ffda78d23c9b.jpg'),
(9, 1, '/uploads/productos/7e038924-045c-45ad-ac70-03b776632dce.png'),
(10, 2, '/uploads/productos/37323ab5-73ad-40ea-a565-ea3aa1ca20af.webp'),
(11, 3, '/uploads/productos/600f2f01-d142-4b77-8472-47c508d1f12d.jpg'),
(12, 4, '/uploads/productos/aace31ab-064d-4b16-a33c-4d4f9c6ffb9a.jpg'),
(14, 6, '/uploads/productos/cf61ea58-785c-4af0-b065-4811702a1428.jpg'),
(15, 7, '/uploads/productos/582646df-5d6d-4b78-8a5c-51d3f7b0ff9b.jpg'),
(16, 8, '/uploads/productos/5b6a365f-9bca-4485-8357-7912688bfd50.jpg'),
(17, 5, '/uploads/productos/918e3991-d30b-4e8f-bc5a-ead4d46bce81.jpg'),
(18, 9, '/uploads/productos/7f37961c-46b1-493b-9486-ac4199fe11bd.jpg'),
(19, 10, '/uploads/productos/e0564942-46e8-4e7d-806e-725b7cb0805c.jpg');

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