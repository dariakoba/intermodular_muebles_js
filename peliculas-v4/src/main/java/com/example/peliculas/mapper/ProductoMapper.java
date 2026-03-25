package com.example.peliculas.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.peliculas.entity.Producto;

public class ProductoMapper implements RowMapper<Producto> {
	@Override
    public Producto mapRow(ResultSet rs) throws SQLException {
        return new Producto(
        		rs.getInt("id"), rs.getString("nombre")
        				, rs.getString("color"), rs.getFloat("precio"), rs.getInt("stock"), rs.getString("descripcion")
        				, rs.getInt("id")
        );
    }
}
