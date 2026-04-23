package com.example.peliculas.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.peliculas.entity.ProductoImagen;

public class ProductoImagenMapper implements RowMapper<ProductoImagen>{

	@Override
	public ProductoImagen mapRow(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return new ProductoImagen(
				rs.getInt("id"),
				rs.getInt("director_id"), 
				rs.getString("url")
		);
	}
	
}
