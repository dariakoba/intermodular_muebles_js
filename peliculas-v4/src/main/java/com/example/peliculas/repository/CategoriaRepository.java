package com.example.peliculas.repository;

import java.sql.Connection;
import java.util.List;

import com.example.peliculas.entity.Categoria;
import com.example.peliculas.mapper.RowMapper;
import com.example.peliculas.mapper.CategoriaMapper;

import com.example.peliculas.dto.ProductoResumen;
import com.example.peliculas.db.DB;
import com.example.peliculas.dto.ProductoDetalle;

public class CategoriaRepository extends BaseRepository<Categoria> {

	public CategoriaRepository(Connection con) {
		super(con, new CategoriaMapper());
	}

	public CategoriaRepository(Connection con, RowMapper<Categoria> mapper) {
		super(con, mapper);
	}

	@Override
	public String getTable() {
		return "categoria";
	}

	@Override
	public String[] getColumnNames() {
		return new String[] { "id_categoria","nombre","deleted_at"};
	}
	
	@Override
	public void setPrimaryKey(Categoria c, int id) {
		c.setIdCategoria(id);
	}

	@Override
	public Object[] getInsertValues(Categoria c) {
		return new Object[] { c.getIdCategoria(), c.getNombre() };
	}

	@Override
	public Object[] getUpdateValues(Categoria c) {
		return new Object[] {  c.getNombre() , c.getIdCategoria(), c.getDeleted_at()};
	}

	@Override
	public Integer getPrimaryKey(Categoria instance) {
		// TODO Auto-generated method stub
		return null;
	}


	

}
