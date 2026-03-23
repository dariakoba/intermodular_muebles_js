package com.example.peliculas.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.peliculas.dto.UserResponse;
import com.example.peliculas.entity.User;

public class UserResponseMapper implements RowMapper<UserResponse> {

	@Override
	public UserResponse map(ResultSet rs) throws SQLException {
		return new UserResponse(
                rs.getInt("id"),
                rs.getString("rol"),
                rs.getString("telefono"),
                rs.getString("estado"),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getString("direccion"),
                rs.getString("email"),
                rs.getInt("puntos"),
                rs.getInt("nivel_acceso"),
                rs.getFloat("salario"),
                rs.getDate("fecha_alta") != null ? rs.getDate("fecha_alta").toLocalDate() : null
        );
	}

}
