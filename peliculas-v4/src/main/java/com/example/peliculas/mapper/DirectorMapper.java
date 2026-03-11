package com.example.peliculas.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.peliculas.entity.Productos;

public class DirectorMapper implements RowMapper<Productos> {
	@Override
    public Productos map(ResultSet rs) throws SQLException {
        return new Productos(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("pais")
        );
    }
}
