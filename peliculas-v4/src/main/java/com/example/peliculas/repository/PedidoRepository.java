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
        return "id";
    }

    @Override
    public Integer getPrimaryKey(Pedido p) {
        return p.getIdPedido();
    }

    @Override
    public void setPrimaryKey(Pedido p, int id) {
        p.setId(id);
    }

    @Override
    public String[] getColumnNames() {
        
        return new String[] { "id", "fecha_pedido", "fecha_devolucion", "precio", "metodo_pago", "factura", "envio", "id_usuario" };
    }

    @Override
    public Object[] getInsertValues(Pedido p) {
        return new Object[] { 
            p.getFechaPedido(), 
            p.getFechaDevolucion(), 
            p.getPrecio(), 
            p.getMetodoPago(), 
            p.getFactura(), 
            p.getEnvio(), 
            p.getClienteId() 
        };
    }

    @Override
    public Object[] getUpdateValues(Pedido p) {
        return new Object[] { 
            p.getFechaPedido(), 
            p.getFechaDevolucion(), 
            p.getPrecio(), 
            p.getMetodoPago(), 
            p.getFactura(), 
            p.getEnvio(), 
            p.getClienteId(),
            p.getIdPedido() 
        };
    }

    
    public List<Pedido> findByUsuarioId(int userId) {
        String sql = "SELECT * FROM pedidos WHERE id_usuario = ? ORDER BY fecha_pedido DESC";
        try {
            return DB.queryMany(con, sql, mapper, userId);
        } catch (SQLException e) {
            throw new DataAccessException("Error buscando los pedidos del usuario: " + userId, e);
        }
    }
}