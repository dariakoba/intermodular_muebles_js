package com.example.peliculas.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.peliculas.entity.Categoria;

public class CategoriaMapper implements RowMapper<Categoria> {
	@Override
    public Categoria mapRow(ResultSet rs) throws SQLException {
        return new Categoria(
                rs.getInt("id_categoria"),
                rs.getString("nombre")
        );
    }
}
