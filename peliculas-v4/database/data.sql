-- =====================================
-- 1. DROP TABLES (Ordenado por dependencias)
-- =====================================
DROP TABLE IF EXISTS resenyas;
DROP TABLE IF EXISTS linea_pedido;
DROP TABLE IF EXISTS pedidos;
DROP TABLE IF EXISTS ejemplar;
DROP TABLE IF EXISTS productos;
DROP TABLE IF EXISTS categoria;
DROP TABLE IF EXISTS usuarios;

-- =====================================
-- 2. CREATE TABLES
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

CREATE TABLE resenyas ( 
    id INT AUTO_INCREMENT PRIMARY KEY, 
    comentario TEXT, 
    puntuacion INT NOT NULL CHECK (puntuacion >= 1 AND puntuacion <= 5), 
    fecha_publicacion DATE DEFAULT (CURRENT_DATE), 
    id_usuario INT NOT NULL, 
    id_producto INT NOT NULL, 
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE, 
    -- CORRECCIÓN: Referencia a productos(id)
    FOREIGN KEY (id_producto) REFERENCES productos(id) ON DELETE CASCADE 
);

-- =====================================
-- 3. INSERTS
-- =====================================
INSERT INTO usuarios (password_hash, rol, telefono, estado, nombre, apellidos, direccion, email, fecha_alta) VALUES
('hash1','admin','600111111','activo','Admin','Principal','Calle Admin 1','admin@dna.com','2024-01-01'),
('hash2','cliente','600222222','activo','Laura','García','Calle Sofa 12','laura@gmail.com','2024-02-01'),
('hash3','cliente','600333333','activo','Carlos','Pérez','Av Mesa 45','carlos@gmail.com','2024-02-10'),
('hash4','cliente','600444444','activo','Marta','López','Calle Silla 7','marta@gmail.com','2024-03-05'),
('hash5','cliente','600555555','activo','David','Ruiz','Av Mueble 22','david@gmail.com','2024-03-15');

INSERT INTO categoria (nombre) VALUES ('Sofás'), ('Mesas'), ('Sillas'), ('Camas'), ('Armarios'), ('Estanterías'), ('Muebles TV'), ('Escritorios');

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

INSERT INTO resenyas (comentario, puntuacion, fecha_publicacion, id_usuario, id_producto) VALUES
('El sofá es súper cómodo.', 5, '2024-03-10', 2, 1),
('La mesa llegó con un rasguño.', 3, '2024-03-12', 3, 3),
('La silla es básica pero cumple.', 4, '2024-03-15', 4, 5);

-- =====================================
-- 4. SELECT FINAL (CORREGIDO)
-- =====================================
SELECT 
    r.id AS resenya_no,
    u.nombre AS cliente,
    p.nombre AS producto,
    r.puntuacion,
    r.comentario
FROM resenyas r
JOIN usuarios u ON r.id_usuario = u.id
JOIN productos p ON r.id_producto = p.id;