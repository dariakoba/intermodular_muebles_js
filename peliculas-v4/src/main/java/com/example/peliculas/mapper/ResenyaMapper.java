package com.example.peliculas.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.peliculas.entity.Resenya;

public class ResenyaMapper implements RowMapper<Resenya> {
    @Override
    public Resenya mapRow(ResultSet rs) throws SQLException {
        Resenya r = new Resenya();
        
        // Ajustado a los nombres reales de la tabla 'resenas'
        r.setIdResenya(rs.getInt("id_resena")); 
        r.setUsuarioId(rs.getInt("id_usuario"));
        r.setProductoId(rs.getInt("id_producto"));
        r.setPuntuacion(rs.getInt("puntuacion"));
        r.setComentario(rs.getString("comentario"));
        
        // Cambiado de 'fecha_publicacion' a 'fecha'
        if (rs.getDate("fecha") != null) {
            r.setFechaPublicacion(rs.getDate("fecha").toLocalDate());
        }
        
        return r;
    }
}