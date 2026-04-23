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
        p.setIdUsuario(rs.getInt("id_usuario"));
        
        java.sql.Timestamp ts = rs.getTimestamp("fecha");
        if (ts != null) {
            p.setFecha(ts.toLocalDateTime().toLocalDate());
        }

        try { p.setEmail(rs.getString("email")); } catch (SQLException e) {}
        try { p.setTelefono(rs.getString("telefono")); } catch (SQLException e) {}

        try {
            p.setNombreProducto(rs.getString("nombre_producto"));
        } catch (SQLException e) {
            p.setNombreProducto(null);
        }
        
        return p;
    }
}