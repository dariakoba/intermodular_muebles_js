package muebles.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import muebles.db.DB;
import muebles.dto.ImagenResponse;
import muebles.entity.ProductoImagen;
import muebles.exception.DataAccessException;
import muebles.mapper.ProductoImagenMapper;
import muebles.mapper.RowMapper;

public class ProductoImagenRepository extends BaseRepository<ProductoImagen> {
	
	public ProductoImagenRepository(Connection con) {
		super(con, new ProductoImagenMapper() );
		// TODO Auto-generated constructor stub
	}
	public ProductoImagenRepository(Connection con, RowMapper<ProductoImagen> mapper) {
		super(con, mapper);
		// TODO Auto-generated constructor stub
	}
	
	
	
	@Override
	public Integer getPrimaryKey(ProductoImagen instance) {
		// TODO Auto-generated method stub
		return instance.getId();
	}
	
	
	@Override
	public String getTable() {
		// TODO Auto-generated method stub
		return "producto_imagenes";
	}
	@Override
	public String getPrimaryKeyName() {
		return "id";
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
