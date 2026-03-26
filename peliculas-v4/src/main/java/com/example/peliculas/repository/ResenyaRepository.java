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
    public String getTable() { return "resenyas"; }

    @Override
    public Integer getPrimaryKey(Resenya instance) { return instance.getIdResenya(); }

    @Override
    public void setPrimaryKey(Resenya instance, int id) { instance.setIdResenya(id); }

    @Override
    public String[] getColumnNames() {
        return new String[] {"id", "id_usuario", "id_producto", "puntuacion", "comentario", "fecha_publicacion"};
    }

    @Override
    public Object[] getInsertValues(Resenya r) {
        return new Object[] { r.getUsuarioId(), r.getProductoId(), r.getPuntuacion(), r.getComentario(), r.getFechaPublicacion() };
    }

    @Override
    public Object[] getUpdateValues(Resenya r) {
        return new Object[] { r.getUsuarioId(), r.getProductoId(), r.getPuntuacion(), r.getComentario(), r.getFechaPublicacion(), r.getIdResenya() };
    }

    public List<Resenya> findByProducto(int idProducto) {
        String sql = "SELECT * FROM resenyas WHERE id_producto = ?";
        try {
          
            return DB.queryMany(con, sql, mapper, idProducto);
        } catch (SQLException e) {
            
            throw translate(e);
        }
    }
}