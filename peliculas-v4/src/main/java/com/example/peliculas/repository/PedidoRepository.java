package com.example.peliculas.repository;

import java.sql.Connection;
import com.example.peliculas.entity.Pedido;
import com.example.peliculas.mapper.PedidoMapper;

public class PedidoRepository extends BaseRepository<Pedido> {

    public PedidoRepository(Connection con) {
        super(con, new PedidoMapper());
    }

    @Override
    public String getPrimaryKeyName() {
        return "id";
    }

    @Override
    public String getTable() {
        return "pedidos";
    }

    @Override
    public String[] getColumnNames() {
        return new String[] { "id", "fecha_pedido", "fecha_devolucion", "precio", "metodo_pago", "factura", "envio", "id_cliente" };
    }
    
    @Override
    public void setPrimaryKey(Pedido p, int id) {
        p.setId(id);
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
            p.getIdCliente() 
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
            p.getIdCliente(),
            p.getId() 
        };
    }

	@Override
	public Integer getPrimaryKey(Pedido instance) {
		// TODO Auto-generated method stub
		return null;
	}
}