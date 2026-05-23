package com.dna.muebles.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.dna.muebles.db.DB;
import com.dna.muebles.entity.Pedido;
import com.dna.muebles.exception.DataAccessException;
import com.dna.muebles.mapper.PedidoMapper;

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
            "GROUP_CONCAT(CONCAT(m.id_producto, '#', d.cantidad, 'x ', m.nombre, ' : ', (d.cantidad * d.precio_unitario), '€') SEPARATOR '|') as nombre_producto " +
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

    // --- MÉTODO NUEVO PARA LA EDICIÓN COMPLETA DEL MODAL DE ADMIN ---
    public void actualizarPedidoCompleto(int idPedido, String estado, String clienteNombre, String fecha, String email, String telefono, String direccion) throws SQLException {
        
        String sqlPedido = "UPDATE pedidos SET estado_pago = ?, cliente_nombre = ?, fecha = ? WHERE id_pedido = ?";
        DB.update(con, sqlPedido, estado, clienteNombre, fecha, idPedido);

        String sqlBusqueda = "SELECT id_usuario FROM pedidos WHERE id_pedido = ?";
        try (java.sql.PreparedStatement ps = con.prepareStatement(sqlBusqueda)) {
            ps.setInt(1, idPedido);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int idUsuario = rs.getInt("id_usuario");
                    String sqlUsuario = "UPDATE usuarios SET email = ?, telefono = ?, direccion = ? WHERE id = ?";
                    DB.update(con, sqlUsuario, email, telefono, direccion, idUsuario);
                }
            }
        }
    }
}