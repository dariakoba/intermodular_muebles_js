package com.example.peliculas.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import org.springframework.dao.DuplicateKeyException;

import com.example.peliculas.db.DB;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.mapper.RowMapper;

public abstract class BaseRepository<T> {

	protected Connection con;
	protected RowMapper<T> mapper;
	
	protected BaseRepository(Connection con, RowMapper<T> mapper) {
		this.con = con;
		this.mapper = mapper;
	}
	
	public abstract String getTable();
	public String getPrimaryKeyName() {
		return "id";
	}
	public abstract Integer getPrimaryKey(T instance);

	public abstract void setPrimaryKey(T instance, int id);

	public abstract String[] getColumnNames();

	public abstract Object[] getInsertValues(T instance);

	public abstract Object[] getUpdateValues(T instance);
	
	public T find(int id) {
		
		try {
			String sql = "SELECT * FROM " + getTable() + " WHERE " + getPrimaryKeyName() + " = ?";
			return DB.queryOne(con, sql, mapper, id);
		} catch (SQLException e) {
			throw new DataAccessException("Error al buscar el registro con id=" + id + " en la tabla " + getTable(), e);
		}
	}
	
	public List<T> findAll() {
		try {
			String sql = "SELECT * FROM " + getTable();
			return DB.queryMany(con, sql, mapper, new Object[0]);
		} catch (SQLException e) {
			throw new DataAccessException("Error al listar registros de la tabla " + getTable(), e);
		}
	}
	
	public T insert(T instance) {
		try {
			String sql = buildInsertSql();
			int id = DB.insert(con, sql, getInsertValues(instance));
			setPrimaryKey(instance, id);
			return instance;
		}catch (SQLException e) {
			throw translate(e);
		}
		
	}

	public int update(T instance) {
		try {
			String sql = buildUpdateSql();
			return DB.update(con, sql, getUpdateValues(instance));
		} catch (SQLException e) {
			throw new DataAccessException(
				"Error al actualizar registro con id=" + getPrimaryKey(instance) + " en la tabla " + getTable(), e
			);
		}
	}
	
	public boolean delete(int id) {
	    // 1. Verificamos que el nombre de la tabla y PK no sean nulos
	    String table = getTable();
	    String pk = getPrimaryKeyName();
	    
	    if (table == null || pk == null) {
	        throw new DataAccessException("Error: Tabla o Primary Key no definidas en el repositorio.");
	    }

	    try {
	        // 2. Construcción limpia del SQL
	        String sql = "DELETE FROM " + table + " WHERE " + pk + " = ?";
	        
	        // 3. Ejecución. DB.delete debe retornar el número de filas afectadas.
	        int rowsAffected = DB.delete(con, sql, id); 
	        
	        return rowsAffected > 0; // Es mejor > 0 que == 1 por seguridad
	        
	    } catch (SQLException e) {
	        // 4. Captura específica de errores de SQL (como claves foráneas)
	        throw new DataAccessException("No se pudo eliminar el ID " + id + " de " + table + ". " +
	                                     "Es probable que este registro esté vinculado a otras tablas.", e);
	    }
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
	
	
	private String buildInsertSql() {
		List<String> columns = new ArrayList<>(List.of(getColumnNames()));
		columns.remove(getPrimaryKeyName());
		String columnsCsv = String.join(", ", columns);

		String[] placeholders = new String[columns.size()];
		Arrays.fill(placeholders, "?");
		String placeholdersCsv = String.join(", ", placeholders);

		return "INSERT INTO " + getTable() + " (" + columnsCsv + ") VALUES (" + placeholdersCsv + ")";
	}
	
	private String buildUpdateSql() {
		List<String> columns = new ArrayList<>(List.of(getColumnNames()));
		columns.remove(getPrimaryKeyName());

		List<String> assignments = new ArrayList<>();
		for (String column : columns) {
			assignments.add(column + " = ?");
		}

		String set = String.join(", ", assignments);

		return "UPDATE " + getTable() + " SET " + set + " WHERE " + getPrimaryKeyName() + " = ?";
	}
	protected RuntimeException translate(SQLException e) {

		if (e.getErrorCode() == 1062 || "23000".equals(e.getSQLState())) {
			return new DuplicateKeyException("Clave duplicada", e);
		}

		return new DataAccessException(e);
	}
}
