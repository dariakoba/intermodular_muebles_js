package com.example.peliculas.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.peliculas.entity.Resenya;

public class ResenyaMapper implements RowMapper<Resenya> {
    @Override
    public Resenya mapRow(ResultSet rs) throws SQLException {
        Resenya r = new Resenya();
        r.setId(rs.getInt("id"));
        r.setIdUsuario(rs.getInt("id_usuario"));
        r.setIdProducto(rs.getInt("id_producto"));
        r.setPuntuacion(rs.getInt("puntuacion"));
        r.setComentario(rs.getString("comentario"));
        if (rs.getDate("fecha_publicacion") != null) {
            r.setFechaPublicacion(rs.getDate("fecha_publicacion").toLocalDate());
        }
        return r;
    }
}