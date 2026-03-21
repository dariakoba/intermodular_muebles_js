-- =====================================
-- DROP TABLES
-- =====================================
DROP TABLE IF EXISTS linea_pedido;
DROP TABLE IF EXISTS pedidos;
DROP TABLE IF EXISTS ejemplar;
DROP TABLE IF EXISTS productos;
DROP TABLE IF EXISTS categoria;
DROP TABLE IF EXISTS usuarios;

-- =====================================
-- CREATE TABLES
-- =====================================
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    password_hash VARCHAR(255),
    rol ENUM('admin','cliente') NOT NULL,
    telefono VARCHAR(20),
    estado VARCHAR(20),
    nombre VARCHAR(50),
    apellidos VARCHAR(50),
    direccion VARCHAR(100),
    email VARCHAR(100),
    nivel_acceso INT NULL,
    puntos INT NULL,
    salario DECIMAL(10,2) NULL,
    fecha_alta DATE
);

CREATE TABLE categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50)
);

CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100),
    color VARCHAR(50),
    precio DECIMAL(10,2),
    stock INT,
    descripcion TEXT,
    id_categoria INT,
    FOREIGN KEY (id_categoria) REFERENCES categoria(id)
);

CREATE TABLE ejemplar (
    id INT AUTO_INCREMENT PRIMARY KEY,
    estado VARCHAR(20),
    id_producto INT,
    FOREIGN KEY (id_producto) REFERENCES productos(id)
);

CREATE TABLE pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_pedido DATE,
    fecha_devolucion DATE NULL,
    precio DECIMAL(10,2),
    metodo_pago VARCHAR(50),
    factura VARCHAR(50),
    envio VARCHAR(50),
    id_usuario INT,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
);

CREATE TABLE linea_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT,
    id_ejemplar INT,
    FOREIGN KEY (id_pedido) REFERENCES pedidos(id),
    FOREIGN KEY (id_ejemplar) REFERENCES ejemplar(id)
);