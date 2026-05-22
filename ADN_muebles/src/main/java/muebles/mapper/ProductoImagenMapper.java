package muebles.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import muebles.entity.ProductoImagen;

public class ProductoImagenMapper implements RowMapper<ProductoImagen>{

	@Override
	public ProductoImagen mapRow(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return new ProductoImagen(
				rs.getInt("id"),
				rs.getInt("producto_id"), 
				rs.getString("url")
		);
	}
	
}
