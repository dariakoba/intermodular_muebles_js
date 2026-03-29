package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
                // Opcional: mantener la contraseña antigua si no envían nueva
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
    public void destroy(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            UserRepository repo = new UserRepository(con);
            repo.delete(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}