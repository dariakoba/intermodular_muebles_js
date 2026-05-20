package com.example.peliculas.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.example.peliculas.db.DB;
import com.example.peliculas.entity.Pedido;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.mapper.PedidoMapper;

public class PedidoRepository extends BaseRepository<Pedido> {

    public PedidoRepository(Connection con) {
        super(con, new PedidoMapper());
    }

    @Override
    public String getTable() {
        return "pedidos";
    }

    @Override
    public String getPrimaryKeyName() {
        return "id_pedido";
    }

    @Override
    public Integer getPrimaryKey(Pedido p) {
        return p.getIdPedido();
    }

    @Override
    public void setPrimaryKey(Pedido p, int id) {
        p.setIdPedido(id);
    }

    @Override
    public String[] getColumnNames() {
        return new String[] { "fecha", "cliente_nombre", "total", "metodo_pago", "estado_pago", "activo", "id_usuario", "puntos_usados" };
    }

    @Override
    public Object[] getInsertValues(Pedido p) {
        return new Object[] { 
            p.getFecha(), 
            p.getClienteNombre(), 
            p.getTotal(), 
            p.getMetodoPago(), 
            p.getEstadoPago(),
            1, // activo
            p.getIdUsuario(),
            p.getPuntosUsados()
        };
    }

    @Override
    public Object[] getUpdateValues(Pedido p) {
        return new Object[] { 
            p.getFecha(), 
            p.getClienteNombre(), 
            p.getTotal(), 
            p.getMetodoPago(), 
            p.getEstadoPago(),
            1, // activo
            p.getIdUsuario(),
            p.getPuntosUsados(), 
            p.getIdPedido() 
        };
    }

    // Método para guardar los detalles (las cantidades)
    public void guardarDetalle(int idPedido, int idProducto, int cantidad, float precio) throws SQLException {
        String sql = "INSERT INTO detalles_pedidos (id_pedido, id_producto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        DB.update(con, sql, idPedido, idProducto, cantidad, precio);
    }

    @Override
    public List<Pedido> findAll() {
        String sql = "SELECT p.*, u.email, u.telefono, u.direccion, " +
                     "GROUP_CONCAT(CONCAT(d.cantidad, 'x ', m.nombre, ' : ', (d.cantidad * d.precio_unitario), '€') SEPARATOR '|') as nombre_producto " +
                     "FROM pedidos p " +
                     "LEFT JOIN usuarios u ON p.id_usuario = u.id " +
                     "LEFT JOIN detalles_pedidos d ON p.id_pedido = d.id_pedido " +
                     "LEFT JOIN productos m ON d.id_producto = m.id_producto " + 
                     "GROUP BY p.id_pedido " +
                     "ORDER BY p.id_pedido DESC";
        try {
            return DB.queryMany(con, sql, mapper);
        } catch (SQLException e) {
            e.printStackTrace();
            return super.findAll();
        }
    }

    public List<Pedido> findByUsuarioId(Integer userId) {
        String sql = 
            "SELECT p.*, " +
            "GROUP_CONCAT(CONCAT(d.cantidad, 'x ', m.nombre, ' : ', (d.cantidad * d.precio_unitario), '€') SEPARATOR '|') as nombre_producto " +
            "FROM pedidos p " +
            "LEFT JOIN detalles_pedidos d ON p.id_pedido = d.id_pedido " +
            "LEFT JOIN productos m ON d.id_producto = m.id_producto " +
            "WHERE p.id_usuario = ? AND p.activo = 1 " +
            "GROUP BY p.id_pedido " +
            "ORDER BY p.fecha DESC, p.id_pedido DESC";

        try {
            return DB.queryMany(con, sql, mapper, userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error pedidos usuario: " + e.getMessage(), e);
        }
    }

    public void actualizarEstado(int idPedido, String nuevoEstado) throws SQLException {
        String sql = "UPDATE pedidos SET estado_pago = ? WHERE id_pedido = ?";
        try (java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idPedido);
            ps.executeUpdate();
        }
    }

    // --- AQUÍ ESTÁ EL NUEVO MÉTODO QUE GUARDA TODOS LOS DATOS EDITADOS ---
    public void actualizarPedidoCompleto(int idPedido, String estado, String clienteNombre, String fecha, String email, String telefono, String direccion) throws SQLException {
        // 1. Actualizamos los datos propios del pedido
        String sqlPedido = "UPDATE pedidos SET estado_pago = ?, cliente_nombre = ?, fecha = ? WHERE id_pedido = ?";
        try (java.sql.PreparedStatement ps = con.prepareStatement(sqlPedido)) {
            ps.setString(1, estado);
            ps.setString(2, clienteNombre);
            ps.setDate(3, java.sql.Date.valueOf(fecha)); 
            ps.setInt(4, idPedido);
            ps.executeUpdate();
        }

        // 2. Averiguamos de qué usuario es este pedido (CON EL FALLITO CORREGIDO AQUÍ)
        String sqlGetUserId = "SELECT id_usuario FROM pedidos WHERE id_pedido = ?";
        int idUsuario = -1;
        try (java.sql.PreparedStatement ps = con.prepareStatement(sqlGetUserId)) {
            ps.setInt(1, idPedido); // Le decimos a SQL de qué pedido buscar
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idUsuario = rs.getInt(1);
                }
            }
        }

        // 3. Actualizamos los datos del usuario asociado al pedido
        if (idUsuario != -1) {
            String sqlUser = "UPDATE usuarios SET direccion = ?, telefono = ?, email = ? WHERE id = ?";
            try (java.sql.PreparedStatement ps = con.prepareStatement(sqlUser)) {
                ps.setString(1, direccion);
                ps.setString(2, telefono);
                ps.setString(3, email);
                ps.setInt(4, idUsuario);
                ps.executeUpdate();
            }
        }
    }
}