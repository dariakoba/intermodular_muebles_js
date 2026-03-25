package com.example.peliculas.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.peliculas.entity.Pelicula;

public class PeliculaMapper implements RowMapper<Pelicula> {
	@Override
    public Pelicula map(ResultSet rs) throws SQLException {
        return new Pelicula(
                rs.getInt("id"),
                rs.getString("titulo"),
                rs.getInt("anyo"),
                rs.getInt("duracion"),
                rs.getString("sinopsis"),
                rs.getInt("director_id")
        );
    }
}
