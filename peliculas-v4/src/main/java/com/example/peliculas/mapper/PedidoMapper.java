package com.example.peliculas.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.peliculas.entity.Pedido;

public class PedidoMapper implements RowMapper<Pedido> {

	@Override
	public Pedido mapRow(ResultSet rs) throws SQLException {
	    Pedido p = new Pedido();
	    p.setIdPedido(rs.getInt("id_pedido"));
	    p.setClienteNombre(rs.getString("cliente_nombre"));
	    p.setTotal(rs.getFloat("total"));
	    p.setMetodoPago(rs.getString("metodo_pago"));
	    p.setEstadoPago(rs.getString("estado_pago"));
	    
	    if (rs.getDate("fecha") != null) {
	        p.setFecha(rs.getDate("fecha").toLocalDate());
	    }

	    try {
	        String n = rs.getString("nombre_producto");
	        p.setNombreProducto(n != null ? n : "Producto no encontrado");
	    } catch (Exception e) {
	        p.setNombreProducto("Mueble DNA");
	    }
	    return p;
	}
}