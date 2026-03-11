package com.example.peliculas.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.peliculas.entity.Categoria;

public class PeliculaMapper implements RowMapper<Categoria> {
	@Override
    public Categoria map(ResultSet rs) throws SQLException {
        return new Categoria(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getInt("anyo"),
                rs.getInt("duracion"),
                rs.getString("sinopsis"),
                rs.getInt("director_id")
        );
    }
}
