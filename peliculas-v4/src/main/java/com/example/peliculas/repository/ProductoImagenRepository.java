package com.example.peliculas.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.example.peliculas.mapper.ProductoImagenMapper;
import com.example.peliculas.db.DB;
import com.example.peliculas.dto.ImagenResponse;
import com.example.peliculas.entity.ProductoImagen;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.mapper.RowMapper;

public class ProductoImagenRepository extends BaseRepository<ProductoImagen> {
	
	protected ProductoImagenRepository(Connection con) {
		super(con, new ProductoImagenMapper() );
		// TODO Auto-generated constructor stub
	}
	protected ProductoImagenRepository(Connection con, RowMapper<ProductoImagen> mapper) {
		super(con, mapper);
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getTable() {
		// TODO Auto-generated method stub
		return "producto_imagenes";
	}
	@Override
	public Integer getPrimaryKey(ProductoImagen pi) {
		// TODO Auto-generated method stub
		return pi.getId();
	}
	@Override
	public void setPrimaryKey(ProductoImagen pi, int id) {
		// TODO Auto-generated method stub
		pi.setId(id);
	}
	@Override
	public String[] getColumnNames() {
		// TODO Auto-generated method stub
		return new String[] {"id","producto_id","url"};
	}
	@Override
	public Object[] getInsertValues(ProductoImagen pi) {
		// TODO Auto-generated method stub
		return new Object[] {pi.getProductoId(),pi.getUrl()};
	}
	@Override
	public Object[] getUpdateValues(ProductoImagen pi) {
		// TODO Auto-generated method stub
		return new Object[] {pi.getProductoId(),pi.getUrl(),pi.getId()};
	}
	
	public List<ImagenResponse> findByProductoId(int productoId) {

		String sql = """
					SELECT id, url
					FROM producto_imagenes
					WHERE producto_id = ?
					ORDER BY id ASC
				""";

		try {
			return DB.queryMany(con, sql, rs -> new ImagenResponse(
					rs.getInt("id"),
					rs.getString("url")
			), productoId);
			
		} catch (SQLException e) {
			throw new DataAccessException("Error Obteniendo las imágenes del producto");
		}
	}
	
	
}
