package com.example.peliculas.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.example.peliculas.entity.Pelicula;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.mapper.RowMapper;
import com.example.peliculas.mapper.PeliculaMapper;

import com.example.peliculas.dto.PeliculaResumen;
import com.example.peliculas.dto.Rating;
import com.example.peliculas.dto.UserVote;
import com.example.peliculas.dto.VotoDTO;
import com.example.peliculas.db.DB;
import com.example.peliculas.dto.PeliculaDetalleDTO;

public class PeliculaRepository extends BaseRepository<Pelicula> {

	public PeliculaRepository(Connection con) {
		super(con, new PeliculaMapper());
	}

	public PeliculaRepository(Connection con, RowMapper<Pelicula> mapper) {
		super(con, mapper);
	}

	@Override
	public String getTable() {
		return "peliculas";
	}

	@Override
	public String[] getColumnNames() {
		return new String[] { "id", "titulo", "anyo", "duracion", "sinopsis", "director_id" };
	}
	
	@Override
	public Integer getPrimaryKey(Pelicula p) {
		return p.getId();
	}
	
	@Override
	public void setPrimaryKey(Pelicula p, int id) {
		p.setId(id);
	}

	@Override
	public Object[] getInsertValues(Pelicula p) {
		return new Object[] { p.getTitulo(), p.getAnyo(), p.getDuracion(), p.getSinopsis(), p.getDirectorId() };
	}

	@Override
	public Object[] getUpdateValues(Pelicula p) {
		return new Object[] { p.getTitulo(), p.getAnyo(), p.getDuracion(), p.getSinopsis(), p.getDirectorId(), p.getId() };
	}

	public List<PeliculaResumen> findResumen() {
		String sql = """
			SELECT id, titulo, anyo
			FROM peliculas
			ORDER BY titulo
		""";
		
		try {
			return DB.queryMany(con, sql, rs ->
				new PeliculaResumen(
					rs.getInt("id"),
					rs.getString("titulo"),
					rs.getInt("anyo")
				)
			);
		} catch (SQLException e) {
			throw new DataAccessException("Error al buscar el listado resumido de películas", e);
		}
	}
	
	public PeliculaDetalleDTO findDetalle(int peliculaId, Integer userId) {
	    
		String sql = """
				SELECT
				    p.id,
				    p.titulo,
				    p.anyo,
				    p.duracion,
				    p.sinopsis,
				    d.nombre AS director,

				    ROUND(AVG(v.puntuacion), 1) AS media,
				    COUNT(v.id) AS total

				FROM peliculas p
				JOIN directores d ON d.id = p.director_id
				LEFT JOIN votos v ON v.pelicula_id = p.id

				WHERE p.id = ?

				GROUP BY p.id
		""";

		
		
		try {
			PeliculaDetalleDTO dto;
			
			dto = DB.queryOne(con, sql, rs -> {

			    PeliculaDetalleDTO p = new PeliculaDetalleDTO();

			    p.id = rs.getInt("id");
			    p.titulo = rs.getString("titulo");
			    p.anyo = rs.getInt("anyo");
			    p.duracion = rs.getInt("duracion");
			    p.sinopsis = rs.getString("sinopsis");
			    p.director = rs.getString("director");

			    p.rating = new Rating(rs.getDouble("media"), rs.getInt("total"));

			    return p;
			    
			}, peliculaId);
			
			dto.ultimosVotos = findUltimosVotos(peliculaId);
		    dto.userVote = userId != null
		    	? findUserVote(peliculaId, userId)
		    	: null;
		
		    return dto;
		    
		} catch (SQLException e) {
			throw new DataAccessException("Error al buscar el detalle de la película con  id " + peliculaId, e);
		}
	}
	
	private List<VotoDTO> findUltimosVotos(int peliculaId) throws SQLException {

		String sql = """
				SELECT
					v.id,
					v.puntuacion,
					v.critica,
					v.fecha,
					u.name AS user

				FROM votos v
				JOIN users u ON u.id = v.user_id

				WHERE v.pelicula_id = ?

				ORDER BY v.fecha DESC

				LIMIT 3
		""";

	    return DB.queryMany(con, sql, rs -> {
	        return new VotoDTO(
	        	rs.getInt("id"),
	        	rs.getString("user"),
	        	rs.getInt("puntuacion"),
	        	rs.getString("critica"),
	        	rs.getDate("fecha").toLocalDate()
	        );
	     },
	    peliculaId);
	}
	
	private UserVote findUserVote(int peliculaId, int userId) throws SQLException {

		String sql = """
				SELECT
					puntuacion,
					critica,
					fecha
				FROM votos 
				WHERE pelicula_id = ? AND user_id = ?
		""";

	    return DB.queryOne(con, sql, rs -> {
	        return new UserVote(
	        	rs.getInt("puntuacion"),
	        	rs.getString("critica"),
	        	rs.getDate("fecha").toLocalDate()
	        );
	    },
	    peliculaId,
	    userId
	    );
	}
}
