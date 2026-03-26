package com.example.peliculas.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.peliculas.entity.LineaPedido;

public class LineaPedidoMapper implements RowMapper<LineaPedido> {
    @Override
    public LineaPedido mapRow(ResultSet rs) throws SQLException {
        return new LineaPedido(
            rs.getInt("id_linea_pedido"),
            rs.getInt("cantidad")
        );
    }
}