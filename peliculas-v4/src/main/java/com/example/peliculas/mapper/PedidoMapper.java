package com.example.peliculas.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.peliculas.entity.Pedido;

public class PedidoMapper implements RowMapper<Pedido> {

    @Override
    public Pedido mapRow(ResultSet rs) throws SQLException {
        
        return new Pedido(
            rs.getInt("id_pedido"),
            rs.getTimestamp("fecha").toLocalDateTime().toLocalDate(),
            rs.getString("cliente_nombre"),
            rs.getFloat("total"),
            rs.getString("metodo_pago"),
            rs.getString("estado_pago")
        );
    }
}