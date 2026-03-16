package com.example.peliculas.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.peliculas.entity.Categoria;
import com.example.peliculas.entity.User;

public class UserMapper implements RowMapper<User> {
	@Override
    public User map(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("password_hash"),
                rs.getString("rol"),
                rs.getString("telefono"),
                rs.getString("estado"),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getString("direccion"),
                rs.getString("email"),
                rs.getInt("puntos"),
                rs.getInt("nivel_acceso"),
                rs.getFloat("salario")
        );
    }
}
