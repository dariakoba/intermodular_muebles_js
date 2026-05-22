package muebles.repository;

import java.sql.Connection;
import java.sql.SQLException;

import muebles.db.DB;
import muebles.exception.DataAccessException;
import muebles.mapper.RowMapper;

public abstract class SoftDeleteRepository<T> extends BaseRepository<T> {


	protected SoftDeleteRepository(Connection con, RowMapper<T> mapper) {
		super(con, mapper);
		// TODO Auto-generated constructor stub
	}

	//softdelete generico
	public int softDelete(int id) {
	    try {
	        String sql = "UPDATE " + getTable() +
	                     " SET deleted_at = NOW() WHERE " + getPrimaryKeyName() + " = ?";
	        return DB.update(con, sql, id);
	    } catch (SQLException e) {
	        throw new DataAccessException("Error al desactivar en " + getTable(), e);
	    }
	}
	
	public int softDeleteActivar(int id) {
	    try {
	        String sql = "UPDATE " + getTable() +
	                     " SET deleted_at = null WHERE " + getPrimaryKeyName() + " = ?";
	        return DB.update(con, sql, id);
	    } catch (SQLException e) {
	        throw new DataAccessException("Error al activar en " + getTable(), e);
	    }
	}
}
