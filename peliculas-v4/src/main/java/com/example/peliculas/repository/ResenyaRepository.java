package com.example.peliculas.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.example.peliculas.entity.Resenya;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.mapper.RowMapper;
import com.example.peliculas.mapper.ResenyaMapper;
import com.example.peliculas.db.DB;
import com.example.peliculas.dto.ResenyaDTO;
import com.example.peliculas.dto.Rating;
import com.example.peliculas.dto.UserVoto;
import com.example.peliculas.dto.VotoDTO;

public class ResenyaRepository extends BaseRepository<Resenya> {

    public ResenyaRepository(Connection con) {
        super(con, new ResenyaMapper());
    }

    @Override
    public String getTable() {
        return "resenyas";
    }

    @Override
    public String[] getColumnNames() {
        return new String[] { "id_resenya", "id_cliente", "id_producto", "puntuacion", "comentario" };
    }
    
    @Override
    public Integer getPrimaryKey(Resenya r) {
        return r.getIdResenya();
    }
    
    @Override
    public void setPrimaryKey(Resenya r, int id) {
        r.setIdResenya(id);
    }

    @Override
    public Object[] getInsertValues(Resenya r) {
        return new Object[] { r.getIdCliente(), r.getIdProducto(), r.getPuntuacion(), r.getComentario() };
    }

    @Override
    public Object[] getUpdateValues(Resenya r) {
        return new Object[] { r.getIdCliente(), r.getIdProducto(), r.getPuntuacion(), r.getComentario(), r.getIdResenya() };
    }

    public ResenyaDTO findDetalleProducto(int productoId, Integer clienteId) {
        String sql = """
                SELECT p.id_producto, p.nombre, p.precio, p.descripcion,
                       c.nombre AS categoria,
                       ROUND(AVG(r.puntuacion), 1) AS media,
                       COUNT(r.id_resenya) AS total
                FROM productos p
                JOIN categoria c ON c.id_categoria = p.id_categoria
                LEFT JOIN resenyas r ON r.id_producto = p.id_producto
                WHERE p.id_producto = ?
                GROUP BY p.id_producto, c.nombre
        """;

        try {
            return DB.queryOne(con, sql, rs -> {
                ResenyaDTO p = new ResenyaDTO(); // Ahora usamos el nombre que quieres
                p.id = rs.getInt("id_producto");
                p.nombre = rs.getString("nombre");
                p.precio = rs.getDouble("precio");
                p.descripcion = rs.getString("descripcion");
                p.categoria = rs.getString("categoria");
                p.rating = new Rating(rs.getDouble("media"), rs.getInt("total"));
                return p;
            }, productoId);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public List<Resenya> findByProducto(int productoId) {
        String sql = "SELECT * FROM resenyas WHERE id_producto = ?";
        try {
            return DB.queryMany(con, sql, new ResenyaMapper(), productoId);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    public int insert(Resenya r) {
        String sql = "INSERT INTO resenyas (id_cliente, id_producto, puntuacion, comentario) VALUES (?, ?, ?, ?)";
        try {
            return DB.insert(con, sql, r.getIdCliente(), r.getIdProducto(), r.getPuntuacion(), r.getComentario());
        } catch (SQLException e) {
            // Lanzamos la excepción para que el Controller sepa que algo falló
            throw new DataAccessException("Error al insertar la reseña", e);
        }
    }
}