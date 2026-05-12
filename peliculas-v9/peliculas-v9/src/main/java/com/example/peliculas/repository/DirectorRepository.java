package com.example.peliculas.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.example.peliculas.db.DB;
import com.example.peliculas.dto.DirectorResumenResponse;
import com.example.peliculas.entity.Director;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.mapper.RowMapper;
import com.example.peliculas.mapper.DirectorMapper;

public class DirectorRepository extends BaseRepository<Director> {

	public DirectorRepository(Connection con) {
		super(con, new DirectorMapper());
	}

	public DirectorRepository(Connection con, RowMapper<Director> mapper) {
		super(con, mapper);
	}

	@Override
	public String getTable() {
		return "directores";
	}

	@Override
	public String[] getColumnNames() {
		return new String[] { "id", "nombre", "pais" };
	}
	
	@Override
	public Integer getPrimaryKey(Director d) {
		return d.getId();
	}
	
	@Override
	public void setPrimaryKey(Director p, int id) {
		p.setId(id);
	}

	@Override
	public Object[] getInsertValues(Director d) {
		return new Object[] { d.getNombre(), d.getPais() };
	}

	@Override
	public Object[] getUpdateValues(Director d) {
		return new Object[] { d.getNombre(), d.getPais(), d.getId() };
	}
	
	public List<DirectorResumenResponse> findAllResumen() {

		String sql = """
				SELECT d.id, d.nombre, d.pais,
				(
					SELECT url
					FROM director_imagenes di
					WHERE di.director_id = d.id
					ORDER BY di.id ASC
					LIMIT 1
				) AS imagen
				FROM directores d
				ORDER BY d.nombre
				""";

		try {
			return DB.queryMany(con, sql, 
				rs -> new DirectorResumenResponse(
					rs.getInt("id"), 
					rs.getString("nombre"),
					rs.getString("pais"), 
					rs.getString("imagen")
				)
			);
		} catch (SQLException e) {
			throw new DataAccessException("Error obteniendo el resumen de directores");
		}
	}
}
