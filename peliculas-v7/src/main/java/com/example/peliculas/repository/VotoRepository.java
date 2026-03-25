package com.example.peliculas.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.example.peliculas.db.DB;
import com.example.peliculas.dto.VotoDTO;
import com.example.peliculas.entity.Voto;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.mapper.RowMapper;
import com.example.peliculas.mapper.VotoMapper;

public class VotoRepository extends BaseRepository<Voto> {

	public VotoRepository(Connection con) {
		super(con, new VotoMapper());
	}

	public VotoRepository(Connection con, RowMapper<Voto> mapper) {
		super(con, mapper);
	}

	@Override
	public String getTable() {
		return "votos";
	}

	@Override
	public String[] getColumnNames() {
		return new String[] { "id", "pelicula_id", "user_id", "puntuacion", "critica", "fecha" };
	}
	
	@Override
	public Integer getPrimaryKey(Voto v) {
		return v.getId();
	}
	
	@Override
	public void setPrimaryKey(Voto v, int id) {
		v.setId(id);
	}

	@Override
	public Object[] getInsertValues(Voto v) {
		return new Object[] { v.getPeliculaId(), v.getUserId(), v.getPuntuacion(), v.getCritica(), v.getFecha() };
	}

	@Override
	public Object[] getUpdateValues(Voto v) {
		return new Object[] { v.getPeliculaId(), v.getUserId(), v.getPuntuacion(), v.getCritica(), v.getFecha(), v.getId() };
	}
	
	public boolean exists(int peliculaId, int userId) {
		
		try {
			String sql = "SELECT * FROM votos WHERE pelicula_id = ? AND user_id = ?";
			return DB.queryOne(con, sql, mapper, peliculaId, userId) != null;
		} catch (SQLException e) {
			throw new DataAccessException("Error comprobando si el usuario con ID " + userId + " ha votado la pelicula con ID " + peliculaId, e);
		}
	}
	
	public List<VotoDTO> findByPeliculaId(int peliculaId) {

		String sql = """
					SELECT
					    v.id,
					    u.name AS user,
					    v.puntuacion,
					    v.critica,
					    v.fecha
					FROM votos v
					JOIN users u ON u.id = v.user_id
					WHERE v.pelicula_id = ?
					ORDER BY v.fecha DESC
				""";

		try {
			return DB.queryMany(con, sql, rs -> {
				return new VotoDTO(
					rs.getInt("id"),
					rs.getString("user"),
					rs.getInt("puntuacion"),
					rs.getString("critica"),
					rs.getDate("fecha").toLocalDate()
				);
			}, peliculaId);
		} catch (SQLException e) {
			throw new DataAccessException("Error buscando los votos de la película con ID " + peliculaId);
		}
	}
}
