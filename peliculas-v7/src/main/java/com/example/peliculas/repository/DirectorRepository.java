package com.example.peliculas.repository;

import java.sql.Connection;

import com.example.peliculas.entity.Director;
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
}
