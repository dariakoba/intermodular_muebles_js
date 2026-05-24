CREATE TABLE categoria (
  id_categoria INT(11) NOT NULL,
  nombre VARCHAR(50) DEFAULT NULL,
  deleted_at DATETIME DEFAULT NULL,
  PRIMARY KEY (id_categoria)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE usuarios (
  id INT(11) NOT NULL,
  password_hash VARCHAR(255) DEFAULT NULL,
  rol ENUM('admin','cliente') NOT NULL,
  telefono VARCHAR(20) DEFAULT NULL,
  estado VARCHAR(20) DEFAULT NULL,
  nombre VARCHAR(50) DEFAULT NULL,
  apellidos VARCHAR(50) DEFAULT NULL,
  direccion VARCHAR(100) DEFAULT NULL,
  email VARCHAR(100) DEFAULT NULL,
  puntos INT(11) DEFAULT NULL,
  salario DECIMAL(10,2) DEFAULT NULL,
  fecha_alta DATE DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY unique_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE productos (
  id_producto INT(11) NOT NULL,
  nombre VARCHAR(100) DEFAULT NULL,
  color VARCHAR(50) DEFAULT NULL,
  precio DECIMAL(10,2) DEFAULT NULL,
  stock INT(11) DEFAULT NULL,
  descripcion TEXT DEFAULT NULL,
  categoria_id INT(11) DEFAULT NULL,
  deleted_at DATETIME DEFAULT NULL,
  PRIMARY KEY (id_producto),
  KEY categoria_id (categoria_id),
  CONSTRAINT productos_ibfk_1 FOREIGN KEY (categoria_id)
    REFERENCES categoria (id_categoria) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE pedidos (
  id_pedido INT(11) NOT NULL,
  id_usuario INT(11) DEFAULT NULL,
  fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
  cliente_nombre VARCHAR(100) NOT NULL,
  total DECIMAL(10,2) NOT NULL,
  metodo_pago VARCHAR(50) DEFAULT NULL,
  estado_pago VARCHAR(50) DEFAULT NULL,
  activo TINYINT(1) DEFAULT 1,
  puntos_usados INT(11) DEFAULT 0,
  PRIMARY KEY (id_pedido),
  KEY fk_pedido_usuario (id_usuario),
  CONSTRAINT fk_pedido_usuario FOREIGN KEY (id_usuario)
    REFERENCES usuarios (id) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE detalles_pedidos (
  id INT(11) NOT NULL,
  id_pedido INT(11) NOT NULL,
  id_producto INT(11) NOT NULL,
  cantidad INT(11) NOT NULL,
  precio_unitario DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id),
  KEY id_producto (id_producto),
  KEY id_pedido (id_pedido),
  CONSTRAINT detalles_pedidos_ibfk_1 FOREIGN KEY (id_producto)
    REFERENCES productos (id_producto) ON DELETE CASCADE,
  CONSTRAINT detalles_pedidos_ibfk_2 FOREIGN KEY (id_pedido)
    REFERENCES pedidos (id_pedido) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE producto_imagenes (
  id INT(11) NOT NULL,
  producto_id INT(11) NOT NULL,
  url VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  KEY producto_id (producto_id),
  CONSTRAINT producto_imagenes_ibfk_1 FOREIGN KEY (producto_id)
    REFERENCES productos (id_producto) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE resenas (
  id_resena INT(11) NOT NULL,
  id_producto INT(11) NOT NULL,
  id_usuario INT(11) NOT NULL,
  puntuacion INT(11) NOT NULL,
  comentario TEXT DEFAULT NULL,
  fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_resena),
  KEY fk_resena_producto (id_producto),
  KEY fk_resena_usuario (id_usuario),
  CONSTRAINT fk_resena_producto FOREIGN KEY (id_producto)
    REFERENCES productos (id_producto) ON DELETE CASCADE,
  CONSTRAINT fk_resena_usuario FOREIGN KEY (id_usuario)
    REFERENCES usuarios (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


CREATE TABLE resena_imagenes (
  id INT(11) NOT NULL,
  resena_id INT(11) NOT NULL,
  url VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  KEY fk_resena_imagen (resena_id),
  CONSTRAINT fk_resena_imagen FOREIGN KEY (resena_id)
    REFERENCES resenas (id_resena) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;