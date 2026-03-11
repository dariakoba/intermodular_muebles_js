-- ===============================
-- BORRAR TABLAS SI EXISTEN
-- ===============================

DROP TABLE IF EXISTS linea_pedido;
DROP TABLE IF EXISTS pedidos;
DROP TABLE IF EXISTS ejemplar;
DROP TABLE IF EXISTS productos;
DROP TABLE IF EXISTS categoria;
DROP TABLE IF EXISTS empleados;
DROP TABLE IF EXISTS clientes;
DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS usuarios;

-- ===============================
-- TABLA USUARIOS
-- ===============================

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    telefono VARCHAR(20),
    estado VARCHAR(20),
    nombre VARCHAR(50) NOT NULL,
    apellidos VARCHAR(50) NOT NULL,
    direccion VARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL
) ENGINE=InnoDB;

-- ===============================
-- TABLA ADMIN
-- ===============================

CREATE TABLE admin (
    id INT PRIMARY KEY,
    nivel_de_acceso INT NOT NULL,
    FOREIGN KEY (id) REFERENCES usuarios(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ===============================
-- TABLA CLIENTES
-- ===============================

CREATE TABLE clientes (
    id INT PRIMARY KEY,
    fecha_alta DATE NOT NULL,
    puntos INT DEFAULT 0,
    FOREIGN KEY (id) REFERENCES usuarios(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ===============================
-- TABLA EMPLEADOS
-- ===============================

CREATE TABLE empleados (
    id INT PRIMARY KEY,
    fecha_contratacion DATE NOT NULL,
    salario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id) REFERENCES usuarios(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ===============================
-- TABLA CATEGORIA
-- ===============================

CREATE TABLE categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
) ENGINE=InnoDB;

-- ===============================
-- TABLA PRODUCTOS
-- ===============================

CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    color VARCHAR(30),
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    descripcion TEXT,
    id_categoria INT NOT NULL,
    FOREIGN KEY (id_categoria)
        REFERENCES categoria(id_categoria)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- ===============================
-- TABLA EJEMPLAR
-- ===============================

CREATE TABLE ejemplar (
    id_ejemplar INT AUTO_INCREMENT PRIMARY KEY,
    estado VARCHAR(20) NOT NULL,
    id_producto INT NOT NULL,
    FOREIGN KEY (id_producto)
        REFERENCES productos(id_producto)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- ===============================
-- TABLA PEDIDOS
-- ===============================

CREATE TABLE pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_pedido DATE NOT NULL,
    fecha_devolucion DATE,
    precio DECIMAL(10,2) NOT NULL,
    metodo_pago VARCHAR(50),
    factura VARCHAR(50),
    envio VARCHAR(50),
    id_cliente INT NOT NULL,
    FOREIGN KEY (id_cliente)
        REFERENCES clientes(id)
        ON DELETE CASCADE
) ENGINE=InnoDB;

-- ===============================
-- TABLA LINEA_PEDIDO
-- ===============================

CREATE TABLE linea_pedido (
    id_pedido INT NOT NULL,
    id_ejemplar INT NOT NULL UNIQUE,
    PRIMARY KEY (id_pedido, id_ejemplar),
    FOREIGN KEY (id_pedido)
        REFERENCES pedidos(id)
        ON DELETE CASCADE,
    FOREIGN KEY (id_ejemplar)
        REFERENCES ejemplar(id_ejemplar)
        ON DELETE CASCADE
) ENGINE=InnoDB;