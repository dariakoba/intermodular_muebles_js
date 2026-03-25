package com.example.peliculas.repository;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.peliculas.db.DB;
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
	public abstract void setPrimaryKey(T instance, int id);
	public abstract String[] getColumnNames();
	public abstract Object[] getInsertValues(T instance);
	public abstract Object[] getUpdateValues(T instance);
	
	public T find(int id) {
		String sql = "SELECT * FROM " + getTable() + " WHERE " + getPrimaryKeyName() + " = ?";
		return DB.queryOne(con, sql, mapper, id);
	}
	
	public List<T> findAll() {
		String sql = "SELECT * FROM " + getTable();
		return DB.queryMany(con, sql, mapper, new Object[0]);
	}
	
	public int insert(T instance) {
		String sql = buildInsertSql();
		int id = DB.insert(con, sql, getInsertValues(instance));
		setPrimaryKey(instance, id);
		return id;
	}

	public int update(T instance) {
		String sql = buildUpdateSql();
		return DB.update(con, sql, getUpdateValues(instance));
	}
	
	public boolean delete(int id) {
		String sql = "DELETE FROM  " + getTable() + " WHERE " + getPrimaryKeyName() + " = ?";
		return DB.delete(con, sql, id) == 1;
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
}
