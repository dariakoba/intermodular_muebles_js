-- =========================
-- TABLA USUARIOS
-- =========================
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    password_hash VARCHAR(255) NOT NULL,
    rol ENUM('admin', 'cliente', 'empleado') NOT NULL,
    telefono VARCHAR(20),
    estado VARCHAR(20),
    nombre VARCHAR(50) NOT NULL,
    apellidos VARCHAR(50) NOT NULL,
    direccion VARCHAR(100),
    email VARCHAR(100) UNIQUE NOT NULL,
    -- Campos específicos
    nivel_acceso INT,
    fecha_alta DATE,
    puntos INT,
    salario DECIMAL(10,2),
    -- Restricciones según rol
    CONSTRAINT chk_admin CHECK (NOT (rol = 'admin' AND (puntos IS NOT NULL OR salario IS NOT NULL))),
    CONSTRAINT chk_cliente CHECK (NOT (rol = 'cliente' AND (nivel_acceso IS NOT NULL OR salario IS NOT NULL))),
    CONSTRAINT chk_empleado CHECK (NOT (rol = 'empleado' AND (nivel_acceso IS NOT NULL OR puntos IS NOT NULL)))
);

-- =========================
-- TABLA CATEGORIA
-- =========================
CREATE TABLE categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

-- =========================
-- TABLA PRODUCTOS
-- =========================
CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    color VARCHAR(30),
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    descripcion TEXT,
    id_categoria INT NOT NULL,
    CONSTRAINT fk_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria) ON DELETE CASCADE,
    CONSTRAINT chk_precio CHECK (precio >= 0),
    CONSTRAINT chk_stock CHECK (stock >= 0)
);

-- =========================
-- TABLA EJEMPLAR
-- =========================
CREATE TABLE ejemplar (
    id_ejemplar INT AUTO_INCREMENT PRIMARY KEY,
    estado VARCHAR(20) NOT NULL,
    id_producto INT NOT NULL,
    CONSTRAINT fk_producto FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE CASCADE
);

-- =========================
-- TABLA PEDIDOS
-- =========================
CREATE TABLE pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_pedido DATE NOT NULL,
    fecha_devolucion DATE,
    precio DECIMAL(10,2) NOT NULL,
    metodo_pago VARCHAR(50),
    factura VARCHAR(50),
    envio VARCHAR(50),
    id_usuario INT NOT NULL,
    CONSTRAINT fk_usuario_pedido FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT chk_precio_pedido CHECK (precio >= 0)
);

-- =========================
-- TABLA LINEA_PEDIDO
-- =========================
CREATE TABLE linea_pedido (
    id_pedido INT NOT NULL,
    id_ejemplar INT NOT NULL UNIQUE,
    PRIMARY KEY (id_pedido, id_ejemplar),
    CONSTRAINT fk_linea_pedido FOREIGN KEY (id_pedido) REFERENCES pedidos(id) ON DELETE CASCADE,
    CONSTRAINT fk_linea_ejemplar FOREIGN KEY (id_ejemplar) REFERENCES ejemplar(id_ejemplar) ON DELETE CASCADE
);

-- =========================
-- TABLAS DE GESTIÓN (M:M)
-- =========================
CREATE TABLE gestion_productos (
    id_usuario INT NOT NULL,
    id_producto INT NOT NULL,
    PRIMARY KEY (id_usuario, id_producto),
    CONSTRAINT fk_gestor_prod FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_prod_gestionado FOREIGN KEY (id_producto) REFERENCES productos(id_producto) ON DELETE CASCADE
);

CREATE TABLE gestion_pedidos (
    id_usuario INT NOT NULL,
    id_pedido INT NOT NULL,
    PRIMARY KEY (id_usuario, id_pedido),
    CONSTRAINT fk_gestor_ped FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_ped_gestionado FOREIGN KEY (id_pedido) REFERENCES pedidos(id) ON DELETE CASCADE
);

CREATE TABLE gestion_ejemplar (
    id_usuario INT NOT NULL,
    id_ejemplar INT NOT NULL,
    PRIMARY KEY (id_usuario, id_ejemplar),
    CONSTRAINT fk_gestor_ejem FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_ejem_gestionado FOREIGN KEY (id_ejemplar) REFERENCES ejemplar(id_ejemplar) ON DELETE CASCADE
);