package com.example.peliculas.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.peliculas.entity.Resenya;

public class ResenyaMapper implements RowMapper<Resenya> {
    @Override
    public Resenya mapRow(ResultSet rs) throws SQLException {
        Resenya r = new Resenya();
        
        // Datos básicos de la tabla 'resenas'
        r.setIdResenya(rs.getInt("id_resena")); 
        r.setUsuarioId(rs.getInt("id_usuario"));
        r.setProductoId(rs.getInt("id_producto"));
        r.setPuntuacion(rs.getInt("puntuacion"));
        r.setComentario(rs.getString("comentario"));
        
        // Fecha (Columna 'fecha' en MariaDB)
        if (rs.getDate("fecha") != null) {
            r.setFechaPublicacion(rs.getDate("fecha").toLocalDate());
        }

        // --- Datos de los JOINs ---
        // Usamos bloques try-catch individuales para que si una query no trae el JOIN,
        // el resto de los datos se sigan mapeando.
        try { r.setNombreUsuario(rs.getString("nombre_usuario")); } catch (SQLException e) {}
        try { r.setEmailUsuario(rs.getString("email_usuario")); } catch (SQLException e) {}
        try { r.setNombreProducto(rs.getString("nombre_producto")); } catch (SQLException e) {}
        
        // Soporte para alias alternativo 'nombre_autor' (usado en tu findByProducto)
        try { 
            if (r.getNombreUsuario() == null) {
                r.setNombreUsuario(rs.getString("nombre_autor"));
            }
        } catch (SQLException e) {}

        return r;
    }
}