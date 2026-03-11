package com.example.peliculas.repository;

import java.sql.Connection;

import com.example.peliculas.entity.Productos;
import com.example.peliculas.mapper.RowMapper;
import com.example.peliculas.mapper.DirectorMapper;

public class DirectorRepository extends BaseRepository<Productos> {

	public DirectorRepository(Connection con) {
		super(con, new DirectorMapper());
	}

	public DirectorRepository(Connection con, RowMapper<Productos> mapper) {
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
	public void setPrimaryKey(Productos p, int id) {
		p.setId(id);
	}

	@Override
	public Object[] getInsertValues(Productos d) {
		return new Object[] { d.getNombre(), d.getPais() };
	}

	@Override
	public Object[] getUpdateValues(Productos d) {
		return new Object[] { d.getNombre(), d.getPais(), d.getId() };
	}
}
