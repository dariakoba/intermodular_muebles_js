package com.example.peliculas.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.example.peliculas.entity.Resenya;
import com.example.peliculas.mapper.RowMapper;
import com.example.peliculas.db.DB; 

public class ResenyaRepository extends BaseRepository<Resenya> {

    public ResenyaRepository(Connection con, RowMapper<Resenya> mapper) {
        super(con, mapper);
    }

    @Override
    public String getTable() { 
        return "resenas"; // Nombre de la tabla en tu SQL
    }

    @Override
    public Integer getPrimaryKey(Resenya instance) { 
        return instance.getIdResenya(); 
    }

    @Override
    public void setPrimaryKey(Resenya instance, int id) { 
        instance.setIdResenya(id); 
    }

    @Override
    public String[] getColumnNames() {
        // Nombres de las columnas en la base de datos MySQL
        return new String[] {"id_usuario", "id_producto", "puntuacion", "comentario", "fecha"};
    }

    @Override
    public Object[] getInsertValues(Resenya r) {
        // Usamos los nombres exactos de tus getters de la entidad Resenya
        return new Object[] { 
            r.getUsuarioId(), 
            r.getProductoId(), 
            r.getPuntuacion(), 
            r.getComentario(), 
            r.getFechaPublicacion() 
        };
    }

    @Override
    public Object[] getUpdateValues(Resenya r) {
        return new Object[] { 
            r.getUsuarioId(), 
            r.getProductoId(), 
            r.getPuntuacion(), 
            r.getComentario(), 
            r.getFechaPublicacion(), 
            r.getIdResenya() 
        };
    }

    public List<Resenya> findByProducto(int idProducto) {
        // IMPORTANTE: r.* trae los datos de la reseña, u.nombre trae el nombre del usuario
        String sql = "SELECT r.*, u.nombre AS nombre_autor " +
                     "FROM resenas r " +
                     "JOIN usuarios u ON r.id_usuario = u.id " + 
                     "WHERE r.id_producto = ?";
        try {
            return DB.queryMany(con, sql, mapper, idProducto);
        } catch (SQLException e) {
            throw translate(e);
        }
    }
}