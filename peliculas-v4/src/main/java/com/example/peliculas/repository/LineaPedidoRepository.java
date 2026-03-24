package com.example.peliculas.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.example.peliculas.db.DB;
import com.example.peliculas.entity.LineaPedido;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.mapper.LineaPedidoMapper;

public class LineaPedidoRepository extends BaseRepository<LineaPedido> {

    public LineaPedidoRepository(Connection con) {
        super(con, new LineaPedidoMapper());
    }

    @Override
    public String getTable() {
        return "linea_pedido";
    }

    @Override
    public String getPrimaryKeyName() {
        // Usamos id_pedido como referencia, aunque sea compuesta
        return "id_pedido";
    }

    @Override
    public Integer getPrimaryKey(LineaPedido lp) {
        return lp.getIdPedido();
    }

    @Override
    public void setPrimaryKey(LineaPedido lp, int id) {
        lp.setIdPedido(id);
    }

    @Override
    public String[] getColumnNames() {
        return new String[] { "id_pedido", "id_ejemplar" };
    }

    @Override
    public Object[] getInsertValues(LineaPedido lp) {
        return new Object[] { lp.getIdPedido(), lp.getIdEjemplar() };
    }

    @Override
    public Object[] getUpdateValues(LineaPedido lp) {
        // En una tabla de muchos a muchos, el update suele afectar a la segunda clave
        return new Object[] { lp.getIdEjemplar(), lp.getIdPedido() };
    }

    /**
     * Método para insertar manualmente saltando la lógica de IDs del BaseRepository
     */
    public void create(LineaPedido lp) {
        String sql = "INSERT INTO linea_pedido (id_pedido, id_ejemplar) VALUES (?, ?)";
        try {
            DB.update(con, sql, lp.getIdPedido(), lp.getIdEjemplar());
        } catch (SQLException e) {
            throw new DataAccessException("Error al insertar la línea de pedido", e);
        }
    }

    /**
     * Obtener todas las líneas de un pedido específico
     */
    public List<LineaPedido> findByPedidoId(int idPedido) {
        String sql = "SELECT * FROM linea_pedido WHERE id_pedido = ?";
        try {
            return DB.queryMany(con, sql, mapper, idPedido);
        } catch (SQLException e) {
            throw new DataAccessException("Error buscando líneas del pedido: " + idPedido, e);
        }
    }
}