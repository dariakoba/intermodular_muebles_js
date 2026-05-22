package com.dna.muebles.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.dna.muebles.db.DB;
import com.dna.muebles.dto.CategoriaDetalle;
import com.dna.muebles.entity.Categoria;
import com.dna.muebles.exception.DataAccessException;
import com.dna.muebles.mapper.CategoriaMapper;
import com.dna.muebles.mapper.RowMapper;


public class CategoriaRepository extends SoftDeleteRepository<Categoria> {

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
	
	public boolean tieneProductos(int idCategoria) throws SQLException {
	    String sql = "SELECT COUNT(*) FROM productos WHERE categoria_id = ?";

	    return DB.queryOne(con, sql, rs -> rs.getInt(1) > 0, idCategoria);
	}

	

}
