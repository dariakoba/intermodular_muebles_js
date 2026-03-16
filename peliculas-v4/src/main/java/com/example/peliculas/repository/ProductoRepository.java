package com.example.peliculas.repository;

import java.sql.Connection;
import java.util.List;

import com.example.peliculas.db.DB;
import com.example.peliculas.entity.Producto;
import com.example.peliculas.mapper.RowMapper;
import com.example.peliculas.mapper.ProductoMapper;
import com.example.peliculas.dto.ProductoResumen;

public class ProductoRepository extends BaseRepository<Producto> {

	public ProductoRepository(Connection con) {
		super(con, new ProductoMapper());
	}

	public ProductoRepository(Connection con, RowMapper<Producto> mapper) {
		super(con, mapper);
	}
	@Override
	public String getPrimaryKeyName() {
		return "id_producto";
	}
	@Override
	public String getTable() {
		return "productos";
	}

	@Override
	public String[] getColumnNames() {
		return new String[] { "id_producto", "nombre", "color","precio","stock","descripcion","id_categoria" };
	}
	
	@Override
	public void setPrimaryKey(Producto p, int id) {
		p.setIdProducto(id);
	}

	@Override
	public Object[] getInsertValues(Producto p) {
		return new Object[] { p.getNombre(), p.getColor(), p.getPrecio(), p.getStock(), p.getDescripcion(), p.getCategoriaId() };
	}

	@Override
	public Object[] getUpdateValues(Producto p) {
		return new Object[] { p.getNombre(), p.getColor(), p.getPrecio(), p.getStock(), p.getDescripcion() ,p.getCategoriaId(),p.getIdProducto() };
	}
	
	public List<ProductoResumen> findResumen(){
		String sql="select nombre, precio from productos";
		return DB.queryMany(con, sql, rs -> 
			new ProductoResumen(rs.getInt("id_producto"), rs.getString("nombre"), rs.getFloat("precio"))
		);
	}
}
