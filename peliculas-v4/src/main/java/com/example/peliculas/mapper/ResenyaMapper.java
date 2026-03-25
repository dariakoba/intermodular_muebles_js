package com.example.peliculas.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.peliculas.entity.Resenya;

public class ResenyaMapper implements RowMapper<Resenya> {
    @Override
    public Resenya mapRow(ResultSet rs) throws SQLException {
        Resenya r = new Resenya();
        r.setIdResenya(rs.getInt("id_resenya"));
        r.setIdCliente(rs.getInt("id_cliente"));
        r.setIdProducto(rs.getInt("id_producto"));
        r.setPuntuacion(rs.getInt("puntuacion"));
        r.setComentario(rs.getString("comentario"));
        if (rs.getDate("fecha_resenya") != null) {
            r.setFechaResenya(rs.getDate("fecha_resenya").toLocalDate());
        }
        return r;
    }
}