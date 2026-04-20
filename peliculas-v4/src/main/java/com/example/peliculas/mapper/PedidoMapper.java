package com.example.peliculas.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.peliculas.entity.Pedido;

public class PedidoMapper implements RowMapper<Pedido> {

    @Override
    public Pedido mapRow(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        
        // Mapeo de campos básicos
        p.setIdPedido(rs.getInt("id_pedido"));
        p.setClienteNombre(rs.getString("cliente_nombre"));
        p.setTotal(rs.getFloat("total"));
        p.setMetodoPago(rs.getString("metodo_pago"));
        p.setEstadoPago(rs.getString("estado_pago"));
        
        // Mapeo del ID de Usuario (El dueño del pedido)
        p.setIdUsuario(rs.getInt("id_usuario"));
        
        // Manejo de la fecha
        if (rs.getDate("fecha") != null) {
            p.setFecha(rs.getDate("fecha").toLocalDate());
        }

        // Manejo del nombre del producto (que ahora puede ser una lista)
        try {
            // Intentamos obtener la columna que genera el GROUP_CONCAT en el Repo
            String n = rs.getString("nombre_producto");
            if (n == null || n.isEmpty()) {
                p.setNombreProducto("Sin productos registrados");
            } else {
                p.setNombreProducto(n);
            }
        } catch (SQLException e) {
            // Si la consulta SQL no incluía el JOIN, ponemos un valor por defecto
            p.setNombreProducto("Mueble DNA");
        }
        
        return p;
    }
}