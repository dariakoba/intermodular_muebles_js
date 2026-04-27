package com.example.peliculas.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.example.peliculas.db.DB;
import com.example.peliculas.entity.Producto;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.mapper.RowMapper;
import com.example.peliculas.mapper.ProductoMapper;
import com.example.peliculas.dto.ProductoCatNomDetalle;
import com.example.peliculas.dto.ProductoDetalle;
import com.example.peliculas.dto.ProductoResumen;

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
		return new String[] { "id_producto", "nombre", "color","precio","stock","descripcion","categoria_id", "deleted_at" };
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
		return new Object[] { p.getNombre(), p.getColor(), p.getPrecio(), p.getStock(), p.getDescripcion(), p.getCategoriaId() , p.getDeletedAt() };
	}

	@Override
	public Object[] getUpdateValues(Producto p) {
		return new Object[] { p.getNombre(), p.getColor(), p.getPrecio(), p.getStock(), p.getDescripcion() ,p.getCategoriaId(),p.getDeletedAt(),
				p.getIdProducto() };
	}
	
	public List<ProductoResumen> findResumen(){
		String sql="select id_producto,nombre, descripcion, precio from productos";
		try {
			return DB.queryMany(con, sql, rs -> 
				new ProductoResumen(rs.getInt("id_producto"), rs.getString("nombre"), rs.getString("descripcion"),rs.getFloat("precio"))
			);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DataAccessException("Error al buscar el listado resumido de productos", e);
		}
	}
	
	public List<ProductoDetalle> findDetalle(){
		String sql="""
				select id_producto,nombre, color, precio, stock, descripcion, categoria_id from productos;""";
				
		try {
			return DB.queryMany(con, sql, rs -> 
				new ProductoDetalle(rs.getInt("id_producto"), rs.getString("nombre"), rs.getString("color"), rs.getFloat("precio"),
						rs.getInt("stock"), rs.getString("descripcion"),rs.getString("categoria_id"))
			);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DataAccessException("Error al buscar el listado resumido de productos", e);
		}
	}
	
	public ProductoDetalle findByDetalleId (int id) {
		String sql="select id_producto,nombre, color, precio, stock, descripcion, categoria_id from productos where id_producto=?";
				
		try {
			return DB.queryOne(con, sql, rs -> 
				new ProductoDetalle(rs.getInt("id_producto"), rs.getString("nombre"), rs.getString("color"), rs.getFloat("precio"),
					rs.getInt("stock"), rs.getString("descripcion"),rs.getString("categoria_id")), id
			);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DataAccessException("Error al buscar el listado detallado de productos", e);
		}
	}
	
	public List<ProductoCatNomDetalle> findDetalleCategoria(){
		String sql="""
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
			return DB.queryMany(con, sql, rs -> 
				new ProductoCatNomDetalle(rs.getInt("id_producto"), rs.getString("nombre"), rs.getString("color"), rs.getFloat("precio"),
						rs.getInt("stock"), rs.getString("categoria_nombre"), rs.getString("estado") )
			);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new DataAccessException("Error al buscar el listado detallado CATNOM de productos", e);
		}
	}
	
	

}
