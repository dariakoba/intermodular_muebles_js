package com.example.peliculas.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import com.example.peliculas.db.DB;
import com.example.peliculas.entity.Producto;
import com.example.peliculas.entity.User;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.mapper.RowMapper;
import com.example.peliculas.mapper.UserMapper;
import com.example.peliculas.dto.ProductoResumen;
import com.example.peliculas.dto.UserResponse;
import com.example.peliculas.mapper.UserResponseMapper;

import jakarta.servlet.http.HttpSession;
public class UserRepository extends BaseRepository<User> {

	public UserRepository(Connection con) {
		super(con, new UserMapper());
	}

	public UserRepository(Connection con, RowMapper<User> mapper) {
		super(con, mapper);
	}
	@Override
	public String getPrimaryKeyName() {
		return "id";
	}
	@Override
	public String getTable() {
		return "usuarios";
	}
	@Override
	public Integer getPrimaryKey(User u) {
		return u.getId();
	}
	
	


	
	

	@Override
	public void setPrimaryKey(User u, int id) {
		u.setId(id);
	}
	/*
	@Override
	public Object[] getInsertValues(User u) {
		return new Object[] {
			    
			    u.getPasswordHash(),
			    u.getRol(),
			    u.getTelefono(),
			    u.getEstado(),
			    u.getNombre(),
			    u.getApellidos(),
			    u.getDireccion(),
			    u.getEmail(),
			    u.getPuntos(),
			    u.getNivelAcceso(),
			    u.getSalario()
			};
	}
	*/
	

	
	
	public User findByEmail(String email) {
        
        try {
        	String sql = "select * from usuarios where email = ?";
			return DB.queryOne(con, sql, new UserMapper(), email);
		} catch (SQLException e) {
			throw new DataAccessException("Error al buscar el usuario con email " + email);

		}
    }
	public UserResponse findResponseById(int id) {
	    
	    try {
	    	String sql = "select id, rol, telefono, estado, nombre, apellidos, direccion, email, puntos,  salario, fecha_alta " +
	                 "from usuarios where id = ?";
			return DB.queryOne(con, sql, new UserResponseMapper(), id);
		} catch (SQLException e) {
			throw new DataAccessException("Error al buscar el usuario con id " + id, e);
		}
	}
	public List<UserResponse> findResponses() {
	    try {
		    String sql = "select id, rol, telefono, estado, nombre, apellidos, direccion, email, puntos,  salario, fecha_alta from usuarios";

			return DB.queryMany(con, sql, new UserResponseMapper());
		} catch (SQLException e) {
			throw new DataAccessException("Error obteniendo los usuarios", e);

		}
	}
	public List<UserResponse> findAllResponses() {
		try {
			String sql = "SELECT id, nombre, email, rol FROM usuarios";

			return DB.queryMany(con, sql, new UserResponseMapper());
		} catch (SQLException e) {
			throw new DataAccessException("Error obteniendo los usuarios", e);

		}
	}

	
	@Override
	public String[] getColumnNames() { 
	    return new String[] { 
	        "id", "password_hash", "rol", "telefono", "estado", 
	        "nombre", "apellidos", "direccion", "email", "puntos", 
	        "salario", "fecha_alta", "foto_url" // <-- Añadir aquí
	    };
	}

	@Override
	public Object[] getInsertValues(User u) {
	    return new Object[] {
	        u.getPasswordHash(), u.getRol(), u.getTelefono(), u.getEstado(),
	        u.getNombre(), u.getApellidos(), u.getDireccion(), u.getEmail(),
	        u.getPuntos(), u.getSalario(), LocalDate.now(), 
	        u.getFotoUrl() // <-- Añadir aquí
	    };
	}

	@Override
	public Object[] getUpdateValues(User u) {
	    return new Object[] { 
	        u.getPasswordHash(), u.getRol(), u.getTelefono(), u.getEstado(),
	        u.getNombre(), u.getApellidos(), u.getDireccion(), u.getEmail(),
	        u.getPuntos(), u.getSalario(), u.getFechaAlta(),
	        u.getFotoUrl(), // <-- Añadir aquí
	        u.getId()       // El ID siempre al final para el WHERE
	    };
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
