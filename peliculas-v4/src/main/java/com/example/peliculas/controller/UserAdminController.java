package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.example.peliculas.dto.UserRequest;
import com.example.peliculas.entity.User;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.repository.UserRepository;

@RestController
@RequestMapping("/api/admin/usuarios")
public class UserAdminController {

    private final DataSource ds;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserAdminController(DataSource ds) {
        this.ds = ds;
    }

    @GetMapping
    public List<User> index() throws SQLException {
        try (Connection con = ds.getConnection()) {
            UserRepository repo = new UserRepository(con);
            return repo.findAll();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @GetMapping("/{id}")
    public User show(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            UserRepository repo = new UserRepository(con);
            return repo.find(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @PostMapping
    public User store(@RequestBody UserRequest req) {
        try (Connection con = ds.getConnection()) {
        	System.out.println(req);
            UserRepository repo = new UserRepository(con);

            User user= new User(
            		null,
            		encoder.encode(req.passwordHash()),
            		req.rol(), 
            		req.telefono(),
            		req.estado(), 
            		req.nombre(), 
            		req.apellidos(),
            		req.direccion(), 
            		req.email(), 
            		0, 
            		
            		0f, 
            		LocalDate.now()
            );
            
            repo.insert(user);
            return user;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @PutMapping("/{id}")
    public User update(@PathVariable int id, @RequestBody User user) {
        try (Connection con = ds.getConnection()) {
            UserRepository repo = new UserRepository(con);
            user.setId(id);
            if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
                String hashed = encoder.encode(user.getPasswordHash());
                user.setPasswordHash(hashed);
            } else {
                // mantener la contraseña antigua si no envían nueva
                User oldUser = repo.find(id);
                user.setPasswordHash(oldUser.getPasswordHash());
            }
            repo.update(user);
            return user;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody 
    public Map<String, Object> destroy(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        
        try (Connection con = ds.getConnection()) {
            UserRepository repo = new UserRepository(con);
            
            User user = repo.find(id);
            if (user == null) {
                response.put("success", false);
                response.put("message", "El usuario ya no existe en la base de datos.");
                return response;
            }

            repo.delete(id);
            
            
            response.put("success", true);
            response.put("message", "Usuario eliminado correctamente.");
            
        } catch (Exception e) {
            response.put("success", false);

            Throwable cause = e.getCause();

            if (e instanceof java.sql.SQLIntegrityConstraintViolationException ||
                (e.getMessage() != null && e.getMessage().toLowerCase().contains("foreign"))) {

                response.put("message",
                    "No se puede borrar: el usuario tiene pedidos o productos asociados.");
            } else {
                response.put("message", "Error al borrar: " + e.getMessage());
            }

            e.printStackTrace();
        
        }
        
        return response;
    }
    @PutMapping("/{id}/estado")
    public User toggleEstado(@PathVariable int id, @RequestBody Map<String, String> body) {
        try (Connection con = ds.getConnection()) {
            UserRepository repo = new UserRepository(con);
            User user = repo.find(id);
            
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
            }

            String nuevoEstado = body.get("estado");
            user.setEstado(nuevoEstado);

            
            repo.update(user); 
            
            return user;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}