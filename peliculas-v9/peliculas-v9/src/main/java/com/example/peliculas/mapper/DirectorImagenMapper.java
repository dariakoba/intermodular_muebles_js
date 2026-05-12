package com.example.peliculas.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.peliculas.entity.DirectorImagen;

public class DirectorImagenMapper implements RowMapper<DirectorImagen> {
	@Override
	public DirectorImagen map(ResultSet rs) throws SQLException {
		return new DirectorImagen(
			rs.getInt("id"),
			rs.getInt("director_id"), 
			rs.getString("url")
		);
	}
}
