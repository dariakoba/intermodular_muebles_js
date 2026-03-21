package com.example.peliculas.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.peliculas.config.Config;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.mapper.RowMapper;

public class DB {
	private DB() {
		// Evita instanciación
	}

	public static <T> List<T> queryMany(Connection con, String sql, RowMapper<T> mapper, Object... params) {
		debug(sql, params);
		
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			bindParams(stmt, params);
			ResultSet rs = stmt.executeQuery();

			List<T> objects = new ArrayList<>();
			while (rs.next()) {
				objects.add(mapper.map(rs));
			}

			return objects;

		} catch (SQLException e) {
			throw new DataAccessException("Error ejecutando SQL select: " + sql, e);
		}
	}

	public static <T> T queryOne(Connection con, String sql, RowMapper<T> mapper, Object... params) {
		debug(sql, params);
		
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			bindParams(stmt, params);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return mapper.map(rs);
			}

			return null;

		} catch (SQLException e) {
			throw new DataAccessException("Error ejecutando SQL select: " + sql, e);
		}
	}

	public static int insert(Connection con, String sql, Object... params) {
		debug(sql, params);
		
		try (PreparedStatement stmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
			bindParams(stmt, params);
			stmt.executeUpdate();

			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				return rs.getInt(1);
			}

			throw new DataAccessException("Falta AUTO_INCREMENT en la PK de la tabla");

		} catch (SQLException e) {
			throw new DataAccessException("Error ejecutando SQL insert: " + sql, e);
		}
	}

	public static int update(Connection con, String sql, Object... params) {
		debug(sql, params);
		
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			bindParams(stmt, params);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			throw new DataAccessException("Error ejecutando SQL update: " + sql, e);
		}
	}

	public static int delete(Connection con, String sql, Object... params) {
		return update(con, sql, params);
	}

	private static void bindParams(PreparedStatement stmt, Object[] params) throws SQLException {
		for (int i = 0; i < params.length; i++) {
			stmt.setObject(i + 1, params[i]);
		}
	}

	private static void debug(String sql, Object... params) {
		if (!Config.SQL_DEBUG)
			return;

		System.out.println("SQL: " + sql);
		System.out.println("Params: " + Arrays.toString(params));
	}
}
