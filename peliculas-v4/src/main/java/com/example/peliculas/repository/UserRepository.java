package com.example.peliculas.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	        "salario", "fecha_alta"
	    };
	}

	@Override
	public Object[] getInsertValues(User u) {
	    return new Object[] {
	        u.getPasswordHash(), u.getRol(), u.getTelefono(), u.getEstado(),
	        u.getNombre(), u.getApellidos(), u.getDireccion(), u.getEmail(),
	        u.getPuntos(), u.getSalario(), LocalDate.now(), 
	    };
	}

	@Override
	public Object[] getUpdateValues(User u) {
	    return new Object[] { 
	        u.getPasswordHash(), u.getRol(), u.getTelefono(), u.getEstado(),
	        u.getNombre(), u.getApellidos(), u.getDireccion(), u.getEmail(),
	        u.getPuntos(), u.getSalario(), u.getFechaAlta(),
	        u.getId()       // El ID siempre al final para el WHERE
	    };
	}
	
	// MÉTODO NUEVO: Para actualizar solo la dirección durante la compra
    public void actualizarDireccion(int idUsuario, String nuevaDireccion) {
        String sql = "UPDATE usuarios SET direccion = ? WHERE id = ?";
        
        try (java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setString(1, nuevaDireccion); 
            ps.setInt(2, idUsuario);         
            
            ps.executeUpdate(); // Ejecuta la actualización en MySQL
            
        } catch (SQLException e) {
            throw new DataAccessException("Error al actualizar la dirección en la compra", e);
        }
    }
	public int countAdmins() throws SQLException {
	    // Usamos el nombre de la tabla que ya tienes definido en getTable()
	    String sql = "SELECT COUNT(*) FROM " + getTable() + " WHERE rol = 'admin'";
	    
	    try (PreparedStatement ps = con.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {
	        
	        if (rs.next()) {
	            return rs.getInt(1);
	        }
	        return 0;
	    } catch (SQLException e) {
	        throw new SQLException("Error al contar los administradores", e);
	    }
	}
	
	

}
