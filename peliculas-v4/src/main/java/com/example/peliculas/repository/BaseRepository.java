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
	
	public int insert(T instance) {
		try {
			String sql = buildInsertSql();
			int id = DB.insert(con, sql, getInsertValues(instance));
			setPrimaryKey(instance, id);
			return (int) instance;
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
		try {
			String sql = "DELETE FROM  " + getTable() + " WHERE " + getPrimaryKeyName() + " = ?";
			return DB.delete(con, sql, id) == 1;
		} catch (SQLException e) {
			throw new DataAccessException("Error al eliminar registro con id=" + id + " de la tabla " + getTable(), e);
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
