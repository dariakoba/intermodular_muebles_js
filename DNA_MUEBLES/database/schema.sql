SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

CREATE TABLE categoria (
    id_categoria INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) DEFAULT NULL,
    deleted_at DATETIME DEFAULT NULL
);

CREATE TABLE pedidos (
    id_pedido INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT(11) DEFAULT NULL,
    fecha DATE DEFAULT NULL,
    total DECIMAL(10,2) DEFAULT NULL,
    metodo_pago VARCHAR(50) DEFAULT NULL,
    estado_pago VARCHAR(50) DEFAULT 'Pendiente de pago', 
    puntos_usados INT(11) DEFAULT 0,                     
    direccion_envio VARCHAR(255) DEFAULT NULL,           
    CONSTRAINT pedidos_ibfk_1 FOREIGN KEY (id_usuario) REFERENCES usuarios (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE detalles_pedidos (
    id_detalle INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT(11) DEFAULT NULL,
    id_producto INT(11) DEFAULT NULL,
    cantidad INT(11) DEFAULT 1,                          
    precio_unitario DECIMAL(10,2) DEFAULT NULL,          
    CONSTRAINT det_ped_fk1 FOREIGN KEY (id_pedido) REFERENCES pedidos (id_pedido) ON DELETE CASCADE,
    CONSTRAINT det_ped_fk2 FOREIGN KEY (id_producto) REFERENCES productos (id_producto)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE productos (
    id_producto INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) DEFAULT NULL,
    color VARCHAR(50) DEFAULT NULL,
    precio DECIMAL(10,2) DEFAULT NULL,
    stock INT(11) DEFAULT NULL,
    descripcion TEXT DEFAULT NULL,
    categoria_id INT(11) DEFAULT NULL REFERENCES categoria(id_categoria),
    deleted_at DATETIME DEFAULT NULL
);

CREATE TABLE producto_imagenes (
    id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    producto_id INT(11) NOT NULL REFERENCES productos(id_producto) ON DELETE CASCADE,
    url VARCHAR(255) NOT NULL
);

CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL,
  `password_hash` varchar(255) DEFAULT NULL,
  `rol` enum('admin','cliente') NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `estado` varchar(20) DEFAULT NULL,
  `nombre` varchar(50) DEFAULT NULL,
  `apellidos` varchar(50) DEFAULT NULL,
  `direccion` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `nivel_acceso` int(11) DEFAULT NULL,
  `puntos` int(11) DEFAULT NULL,
  `salario` decimal(10,2) DEFAULT NULL,
  `fecha_alta` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- PRIMARY KEYS
ALTER TABLE `categoria` ADD PRIMARY KEY (`id_categoria`);
ALTER TABLE `linea_pedido` ADD PRIMARY KEY (`id`), ADD KEY `id_pedido` (`id_pedido`), ADD KEY `id_ejemplar` (`id_ejemplar`);
ALTER TABLE `pedidos` ADD PRIMARY KEY (`id`), ADD KEY `id_usuario` (`id_usuario`);
ALTER TABLE `productos` ADD PRIMARY KEY (`id_producto`), ADD KEY `id_categoria` (`categoria_id`);
ALTER TABLE `usuarios` ADD PRIMARY KEY (`id`);

-- AUTO_INCREMENT
ALTER TABLE `categoria` MODIFY `id_categoria` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `linea_pedido` MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `pedidos` MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `productos` MODIFY `id_producto` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `usuarios` MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

-- FOREIGN KEYS
ALTER TABLE `linea_pedido`
  ADD CONSTRAINT `linea_pedido_ibfk_1` FOREIGN KEY (`id_pedido`) REFERENCES `pedidos` (`id`),
  ADD CONSTRAINT `linea_pedido_ibfk_2` FOREIGN KEY (`id_ejemplar`) REFERENCES `ejemplar` (`id`);

ALTER TABLE `pedidos`
  ADD CONSTRAINT `pedidos_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`id`);

ALTER TABLE `productos`
  ADD CONSTRAINT `productos_ibfk_1` FOREIGN KEY (`categoria_id`) REFERENCES `categoria` (`id_categoria`);

  
ALTER TABLE `productos`
  ADD COLUMN deleted_at DATETIME DEFAULT NULL;
COMMIT;