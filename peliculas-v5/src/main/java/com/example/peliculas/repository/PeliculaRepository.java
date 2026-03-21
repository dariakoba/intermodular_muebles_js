package com.example.peliculas.repository;

import java.sql.Connection;
import java.util.List;

import com.example.peliculas.entity.Pelicula;
import com.example.peliculas.mapper.RowMapper;
import com.example.peliculas.mapper.PeliculaMapper;

import com.example.peliculas.dto.PeliculaResumen;
import com.example.peliculas.db.DB;
import com.example.peliculas.dto.PeliculaDetalle;

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
		
		return DB.queryMany(con, sql, rs ->
			new PeliculaResumen(
				rs.getInt("id"),
				rs.getString("titulo"),
				rs.getInt("anyo")
			)
		);
	}
	
	public PeliculaDetalle findDetalle(int id) {
		String sql = """
			SELECT p.titulo,
			       p.anyo,
			       p.duracion,
			       p.sinopsis,
			       d.nombre AS director
			FROM peliculas p
			JOIN directores d ON p.director_id = d.id
			WHERE p.id = ?
		""";
		
		return DB.queryOne(con, sql, rs ->
			new PeliculaDetalle(
				rs.getString("titulo"),
				rs.getInt("anyo"),
				rs.getInt("duracion"),
				rs.getString("sinopsis"),
				rs.getString("director")
			),
			id
		);
	}
}
