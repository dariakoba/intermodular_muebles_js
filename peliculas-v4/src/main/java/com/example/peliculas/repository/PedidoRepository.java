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
        return "id_pedido"; // Ajustado a tu SQL
    }

    @Override
    public Integer getPrimaryKey(Pedido p) {
        return p.getIdPedido();
    }

    @Override
    public void setPrimaryKey(Pedido p, int id) {
        p.setIdPedido(id); // Usa el nombre nuevo de tu entidad
    }

    @Override
    public String[] getColumnNames() {
        // Estas son las columnas REALES de tu tabla en phpMyAdmin
        return new String[] { "fecha", "cliente_nombre", "total", "metodo_pago", "estado_pago" };
    }

    @Override
    public Object[] getInsertValues(Pedido p) {
        return new Object[] { 
            java.sql.Timestamp.valueOf(p.getFecha().atStartOfDay()), // Convierte LocalDate a Timestamp para MySQL
            p.getClienteNombre(), 
            p.getTotal(), 
            p.getMetodoPago(), 
            p.getEstadoPago() 
        };
    }

    @Override
    public Object[] getUpdateValues(Pedido p) {
        return new Object[] { 
            java.sql.Timestamp.valueOf(p.getFecha().atStartOfDay()), 
            p.getClienteNombre(), 
            p.getTotal(), 
            p.getMetodoPago(), 
            p.getEstadoPago(),
            p.getIdPedido() // El ID para el WHERE
        };
    }

    /**
     * Lista todos los pedidos para el panel de administración
     */
    public List<Pedido> findAll() {
        String sql = "SELECT * FROM pedidos ORDER BY fecha DESC";
        try {
            return DB.queryMany(con, sql, mapper);
        } catch (SQLException e) {
            throw new DataAccessException("Error al listar todos los pedidos", e);
        }
    }

    /**
     * Busca pedidos por nombre de cliente (ya que en tu SQL no hay id_usuario)
     */
    public List<Pedido> findByCliente(String nombre) {
        String sql = "SELECT * FROM pedidos WHERE cliente_nombre = ? ORDER BY fecha DESC";
        try {
            return DB.queryMany(con, sql, mapper, nombre);
        } catch (SQLException e) {
            throw new DataAccessException("Error buscando los pedidos del cliente: " + nombre, e);
        }
    }
}