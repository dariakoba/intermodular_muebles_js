package com.example.peliculas.repository;

import java.sql.Connection;
import java.sql.SQLException;
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
	public String[] getColumnNames() { 
	    return new String[] { 
	        "id",
	        "password_hash",
	        "rol",
	        "telefono",
	        "estado",
	        "nombre",
	        "apellidos",
	        "direccion",
	        "email",
	        "puntos",
	        "nivel_acceso",
	        "salario"
	    };
	}


	
	

	@Override
	public void setPrimaryKey(User u, int id) {
		u.setId(id);
	}

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

	@Override
	public Object[] getUpdateValues(User u) {
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
			    u.getSalario(),
			    u.getId()
			};
	}
	
	public User findByEmail(String email) {
        String sql = "select * from usuarios where email = ?";
        return DB.queryOne(con, sql, new UserMapper(), email);
    }
	public UserResponse findResponseById(int id) {
	    String sql = "select id, rol, telefono, estado, nombre, apellidos, direccion, email, puntos, nivel_acceso, salario " +
	                 "from usuarios where id = ?";
	    return DB.queryOne(con, sql, new UserResponseMapper(), id);
	}
	public List<UserResponse> findResponses() {
	    String sql = "select id, rol, telefono, estado, nombre, apellidos, direccion, email, puntos, nivel_acceso, salario from usuarios";
	    return DB.queryMany(con, sql, new UserResponseMapper());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
