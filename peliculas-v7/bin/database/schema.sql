CREATE TABLE directores (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    pais VARCHAR(100) NOT NULL
);

CREATE TABLE peliculas (
  id INTEGER AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(255) NOT NULL,
  anyo SMALLINT UNSIGNED NOT NULL,
  duracion SMALLINT UNSIGNED NOT NULL,
  sinopsis TEXT NOT NULL,
  director_id INTEGER NOT NULL,

  CONSTRAINT fk_peliculas_director
    FOREIGN KEY (director_id)
    REFERENCES directores(id)
);

CREATE TABLE users (
  id INTEGER AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(50) NOT NULL
);

CREATE TABLE votos (

    id INT AUTO_INCREMENT PRIMARY KEY,

    pelicula_id INT NOT NULL,
    user_id INT NOT NULL,

    puntuacion TINYINT UNSIGNED NOT NULL,
    critica TEXT,

    fecha DATE NOT NULL,

    UNIQUE (pelicula_id, user_id),

    CONSTRAINT fk_votos_pelicula
        FOREIGN KEY (pelicula_id)
        REFERENCES peliculas(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_votos_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_puntuacion
        CHECK (puntuacion BETWEEN 1 AND 10)

);