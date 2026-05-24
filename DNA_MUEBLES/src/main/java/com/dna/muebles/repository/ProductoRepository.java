package com.dna.muebles.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.dna.muebles.db.DB;
import com.dna.muebles.dto.ProductoCatNomDetalle;
import com.dna.muebles.dto.ProductoDetalle;
import com.dna.muebles.dto.ProductoResumen;
import com.dna.muebles.dto.ProductoResumenImagen;
import com.dna.muebles.dto.ProductoShowCliente;
import com.dna.muebles.entity.Producto;
import com.dna.muebles.exception.DataAccessException;
import com.dna.muebles.mapper.ProductoMapper;
import com.dna.muebles.mapper.RowMapper;

public class ProductoRepository extends SoftDeleteRepository<Producto> {

	public ProductoRepository(Connection con) {
		super(con, new ProductoMapper());
	}

	public ProductoRepository(Connection con, RowMapper<Producto> mapper) {
		super(con, mapper);
	}

	public String getPrimaryKeyName() {
		return "id_producto";
	}

	@Override
	public String getTable() {
		return "productos";
	}

	@Override
	public String[] getColumnNames() {
		return new String[] { "id_producto", "nombre", "color", "precio", "stock", "descripcion", "categoria_id",
				"deleted_at" };
	}

	@Override
	public Integer getPrimaryKey(Producto p) {
		// TODO Auto-generated method stub
		return p.getIdProducto();
	}

	@Override
	public void setPrimaryKey(Producto p, int id) {
		p.setIdProducto(id);
	}

	@Override
	public Object[] getInsertValues(Producto p) {
		return new Object[] { p.getNombre(), p.getColor(), p.getPrecio(), p.getStock(), p.getDescripcion(),
				p.getCategoriaId(), p.getDeletedAt() };
	}

	@Override
	public Object[] getUpdateValues(Producto p) {
		return new Object[] { p.getNombre(), p.getColor(), p.getPrecio(), p.getStock(), p.getDescripcion(),
				p.getCategoriaId(), p.getDeletedAt(), p.getIdProducto() };
	}

	// cliente
	public List<ProductoResumenImagen> findResumen() {
		String sql = """
				SELECT p.id_producto, p.nombre, p.precio, p.stock, p.categoria_id, p.color
				(
					SELECT url
					FROM producto_imagenes pi
					WHERE pi.producto_id = p.id_producto
					ORDER BY pi.id ASC
					LIMIT 1
				) AS imagen
				FROM productos p
				ORDER BY p.nombre
				""";
		try {
			return DB.queryMany(con, sql,
					rs -> new ProductoResumenImagen(rs.getInt("id_producto"), rs.getString("nombre"),
							rs.getFloat("precio"), rs.getInt("stock"), rs.getString("categoria_id"), rs.getString("color"),
							rs.getString("imagen")));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DataAccessException("Error al buscar el listado resumido de productos", e);
		}
	}

	// admin index
	public List<ProductoResumen> findAllResumenNoImagen() {

		String sql = """
				SELECT p.id_producto, p.nombre, p.precio, p.stock,
				case
				        when p.deleted_at is null then 'activo'
				        else 'inactivo'
				    end as estado,
				c.nombre as categoria_nombre
				FROM productos p
				left join categoria c
				on c.id_categoria = p.categoria_id

				""";

		try {
			return DB.queryMany(con, sql,
					rs -> new ProductoResumen(rs.getInt("id_producto"), rs.getString("nombre"), rs.getFloat("precio"),
							rs.getInt("stock"), rs.getString("estado"), rs.getString("categoria_nombre")));
		} catch (SQLException e) {
			throw new DataAccessException("Error obteniendo el resumen de productos");
		}
	}

	
	//admin show con imagen
	public ProductoDetalle findDetalle(int id) {

	    String sql = """
	        SELECT p.id_producto, p.nombre, p.color, p.precio, p.stock, p.descripcion, c.id_categoria as categoria_id,
	               c.nombre as categoria_nombre,
	               CASE
	                   WHEN p.deleted_at IS NULL THEN 'activo'
	                   ELSE 'inactivo'
	               END as estado
	        FROM productos p
	        LEFT JOIN categoria c ON c.id_categoria = p.categoria_id
	        WHERE p.id_producto = ?
	        """;

	    try {
	    	return DB.queryOne(con, sql, rs -> new ProductoDetalle(
	    		    rs.getInt("id_producto"),
	    		    rs.getString("nombre"),
	    		    rs.getString("color"),
	    		    rs.getFloat("precio"),
	    		    rs.getInt("stock"),
	    		    rs.getString("descripcion"),
	    		    rs.getInt("categoria_id"),
	    		    rs.getString("categoria_nombre"),
	    		    rs.getString("estado"),
	    		    new ArrayList<>()
	    		), id); // ← el id va al final como varargs
	    } catch (SQLException e) {
	        throw new DataAccessException("Error obteniendo detalle del producto");
	    }
	}
	

	//

	public ProductoResumenImagen findByDetalleId(int id) {

		String sql = """
				SELECT p.id_producto, p.nombre, p.precio, p.stock, p.categoria_id,
				(
					SELECT url
					FROM producto_imagenes pi
					WHERE pi.producto_id = p.id_producto
					ORDER BY pi.id ASC
					LIMIT 1
				) AS imagen
				FROM productos p
				where id_producto = ?

				""";

		try {

			return DB.queryOne(con, sql, rs ->

			new ProductoResumenImagen(rs.getInt("id_producto"), rs.getString("nombre"), rs.getFloat("precio"),
					rs.getInt("stock"), rs.getString("categoria_id"), rs.getString("color"), rs.getString("imagen"))

					, id);

		} catch (SQLException e) {

			throw new DataAccessException("Error al buscar el listado detallado de productos", e);
		}
	}

	// cliente productos
	public List<ProductoResumenImagen> findAllResumen() {

		String sql = """
				SELECT p.id_producto, p.nombre, p.precio, p.stock, p.categoria_id, p.color, c.nombre as categoria_nombre,
				(
					SELECT url
					FROM producto_imagenes pi
					WHERE pi.producto_id = p.id_producto
					ORDER BY pi.id ASC
					LIMIT 1
				) AS imagen
				FROM productos p
				left join categoria c on c.id_categoria = p.categoria_id
				where p.deleted_at is null
				""";

		try {
			return DB.queryMany(con, sql,
					rs -> new ProductoResumenImagen(rs.getInt("id_producto"), rs.getString("nombre"),
							rs.getFloat("precio"), rs.getInt("stock"), rs.getString("categoria_nombre"),
							rs.getString("color"),
							rs.getString("imagen")));
		} catch (SQLException e) {
			throw new DataAccessException("Error obteniendo el resumen de directores");
		}
	}

	public List<ProductoCatNomDetalle> findDetalleCategoria() {
		String sql = """
				select
				    p.id_producto,
				    p.nombre,
				    p.color,
				    p.precio,
				    p.stock,
				    case
				        when p.deleted_at is null then 'activo'
				        else 'inactivo'
				    end as estado,
				    c.nombre as categoria_nombre
				from productos p
				left join categoria c
				  on c.id_categoria = p.categoria_id;
				""";

		try {
			return DB.queryMany(con, sql,
					rs -> new ProductoCatNomDetalle(rs.getInt("id_producto"), rs.getString("nombre"),
							rs.getString("color"), rs.getFloat("precio"), rs.getInt("stock"),
							rs.getString("categoria_nombre"), rs.getString("estado")));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DataAccessException("Error al buscar el listado detallado CATNOM de productos", e);
		}
	}

	// Método para descontar stock de forma segura
    public int decrementStock(int idProducto, int cantidad) throws java.sql.SQLException {
        String sql = "UPDATE productos SET stock = stock - ? WHERE id_producto = ? AND stock >= ?";
        
        try (java.sql.PreparedStatement stmt = this.con.prepareStatement(sql)) {
            stmt.setInt(1, cantidad);
            stmt.setInt(2, idProducto);
            stmt.setInt(3, cantidad);
            
            return stmt.executeUpdate();
        }
    }
}
