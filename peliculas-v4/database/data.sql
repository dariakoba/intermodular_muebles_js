-- =============================================
-- Directores
-- =============================================

INSERT INTO directores (nombre, pais) VALUES
('Lana Wachowski', 'Estados Unidos'),
('Christopher Nolan', 'Reino Unido'),
('Steven Spielberg', 'Estados Unidos'),
('Quentin Tarantino', 'Estados Unidos'),
('Hayao Miyazaki', 'Japón');


-- =============================================
-- Películas con director
-- =============================================
INSERT INTO peliculas
(titulo, anyo, duracion, sinopsis, director_id)
VALUES
('Matrix', 1999, 136,
 'Un hacker descubre que la realidad es una simulación.',
 1),

('Inception', 2010, 148,
 'Un ladrón roba secretos infiltrándose en sueños.',
 2),

('Jurassic Park', 1993, 127,
 'Un parque temático de dinosaurios clonados pierde el control.',
 3),

('Pulp Fiction', 1994, 154,
 'Historias criminales entrelazadas en Los Ángeles.',
 4),

('El viaje de Chihiro', 2001, 125,
 'Una niña entra en un mundo mágico gobernado por espíritus.',
 5),
 
('The Matrix Reloaded', 2003, 138,
 'Neo y los rebeldes continúan la lucha contra las máquinas.',
 1),

('The Matrix Revolutions', 2003, 129,
 'La guerra entre humanos y máquinas alcanza su punto final.',
 1),

('Interstellar', 2014, 169,
 'Un grupo de astronautas viaja a través de un agujero de gusano para salvar a la humanidad.',
 2),

('The Dark Knight', 2008, 152,
 'Batman se enfrenta al Joker en una batalla por el alma de Gotham.',
 2),

('Tenet', 2020, 150,
 'Un agente debe manipular el flujo del tiempo para evitar una guerra mundial.',
 2),

('E.T. the Extra-Terrestrial', 1982, 115,
 'Un niño se hace amigo de un extraterrestre perdido en la Tierra.',
 3),

('Indiana Jones and the Last Crusade', 1989, 127,
 'Indiana Jones busca el Santo Grial junto a su padre.',
 3),

('Django Unchained', 2012, 165,
 'Un esclavo liberado busca rescatar a su esposa con ayuda de un cazarrecompensas.',
 4),

('Kill Bill: Vol. 1', 2003, 111,
 'Una asesina busca venganza contra su antiguo escuadrón.',
 4),

('Kill Bill: Vol. 2', 2004, 137,
 'La búsqueda de venganza de la Novia llega a su final.',
 4),

('My Neighbor Totoro', 1988, 86,
 'Dos niñas descubren espíritus del bosque en el campo japonés.',
 5),

('Princess Mononoke', 1997, 134,
 'Un príncipe se ve atrapado en el conflicto entre humanos y dioses del bosque.',
 5);
