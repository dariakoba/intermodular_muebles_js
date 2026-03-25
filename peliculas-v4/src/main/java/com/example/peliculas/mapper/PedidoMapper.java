package com.example.peliculas.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.peliculas.entity.Pedido;

public class PedidoMapper implements RowMapper<Pedido> {
    @Override
    public Pedido mapRow(ResultSet rs) throws SQLException {
        return new Pedido(
            rs.getInt("id"),
            rs.getDate("fecha_pedido").toLocalDate(),
            rs.getDate("fecha_devolucion") != null ? rs.getDate("fecha_devolucion").toLocalDate() : null,
            rs.getFloat("precio"),
            rs.getString("metodo_pago"),
            rs.getString("factura"),
            rs.getString("envio"),
            rs.getInt("id_usuario") 
        );
    }
}