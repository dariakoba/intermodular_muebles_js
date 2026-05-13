package com.example.peliculas.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.peliculas.entity.Voto;

public class VotoMapper implements RowMapper<Voto> {
	@Override
	public Voto map(ResultSet rs) throws SQLException {
		return new Voto(
			rs.getInt("id"), 
			rs.getInt("pelicula_id"),
			rs.getInt("user_id"), 
			rs.getInt("puntuacion"),
			rs.getString("critica"), 
			rs.getDate("fecha").toLocalDate()
		);
	}
}
