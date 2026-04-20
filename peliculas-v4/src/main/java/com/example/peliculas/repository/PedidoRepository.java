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
        // Añadimos id_producto a la lista
        return new String[] { "fecha", "cliente_nombre", "total", "metodo_pago", "estado_pago", "activo", "id_producto" };
    }

    @Override
    public Object[] getInsertValues(Pedido p) {
        return new Object[] { 
            p.getFecha(), 
            p.getClienteNombre(), 
            p.getTotal(), 
            p.getMetodoPago(), 
            p.getEstadoPago(),
            1,
            p.getIdProducto() 
        };
    }

    @Override
    public List<Pedido> findAll() {
        String sql = "SELECT p.*, m.nombre as nombre_producto " +
                     "FROM pedidos p " +
                     "LEFT JOIN productos m ON p.id_producto = m.id_producto " + 
                     "ORDER BY p.id_pedido DESC"; 
        try {
            return DB.queryMany(con, sql, mapper);
        } catch (SQLException e) {
            return super.findAll();
        }
    }

	@Override
	public Object[] getUpdateValues(Pedido instance) {
		// TODO Auto-generated method stub
		return null;
	}
    
}