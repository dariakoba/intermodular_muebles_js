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
        return new String[] { "fecha", "cliente_nombre", "total", "metodo_pago", "estado_pago", "activo", "id_usuario" };
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
            p.getIdUsuario() 
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
        // ---> NUEVO: Hemos añadido u.direccion al SELECT <---
        String sql = "SELECT p.*, u.email, u.telefono, u.direccion, " +
                     "GROUP_CONCAT(CONCAT(d.cantidad, 'x ', m.nombre, ' : ', d.precio_unitario, '€') SEPARATOR '|') as nombre_producto " +
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
            "SELECT p.*, m.nombre as nombre_producto " +
            "FROM pedidos p " +
            "LEFT JOIN productos m ON p.id_producto = m.id_producto " +
            "WHERE p.id_usuario = ? AND p.activo = 1 " +
            "ORDER BY p.fecha DESC";

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
}