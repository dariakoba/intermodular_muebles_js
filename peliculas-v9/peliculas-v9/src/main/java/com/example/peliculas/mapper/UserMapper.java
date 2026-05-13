package com.example.peliculas.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.peliculas.entity.User;

public class UserMapper implements RowMapper<User> {

	@Override
	public User map(ResultSet rs) throws SQLException {
		return new User(
			rs.getInt("id"),
			rs.getString("name"),
			rs.getString("email"),
			rs.getString("password"),
			rs.getString("role")
		);
	}
}
