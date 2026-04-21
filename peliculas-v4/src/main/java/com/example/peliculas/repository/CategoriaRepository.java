package com.example.peliculas.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.example.peliculas.entity.Categoria;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.mapper.RowMapper;
import com.example.peliculas.mapper.CategoriaMapper;

import com.example.peliculas.dto.ProductoResumen;
import com.example.peliculas.db.DB;
import com.example.peliculas.dto.CategoriaDetalle;
import com.example.peliculas.dto.ProductoCatNomDetalle;
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
		return new Object[] { c.getNombre(), c.getDeleted_at() };
	}

	@Override
	public Object[] getUpdateValues(Categoria c) {
		return new Object[] {  c.getNombre() , c.getDeleted_at(), c.getIdCategoria()};
	}

	@Override
	public Integer getPrimaryKey(Categoria instance) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<CategoriaDetalle> findAllCategoria(){
		String sql="""
				select 
				    c.id_categoria,
				    c.nombre,
				    case 
				        when c.deleted_at is null then 'activo'
				        else 'inactivo'
				    end as estado
				from categoria c
				
				""";
				
		try {
			return DB.queryMany(con, sql, rs -> 
				new CategoriaDetalle(rs.getInt("id_categoria"), rs.getString("nombre"), rs.getString("estado") )
			);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DataAccessException("Error al buscar el listado detallado de categorias", e);
		}
	}


	

}
