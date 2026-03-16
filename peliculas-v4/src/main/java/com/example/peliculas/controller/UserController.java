package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.*;

import com.example.peliculas.dto.UserResponse;
import com.example.peliculas.entity.User;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final DataSource ds;

    public UserController(DataSource ds) {
        this.ds = ds;
    }

    // Lista todos los usuarios como DTO (sin contraseña)
    @GetMapping
    public List<UserResponse> indexResponses() {
        try (Connection con = ds.getConnection()) {
            UserRepository repo = new UserRepository(con);
            return repo.findResponses(); // Lista de UserResponse
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    // Obtiene un usuario por id como DTO (sin contraseña)
    @GetMapping("/response/{id}")
    public UserResponse showResponse(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            UserRepository repo = new UserRepository(con);
            return repo.findResponseById(id); // UserResponse
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    // Obtiene un usuario por id como entidad completa (incluye contraseña)
    @GetMapping("/{id}")
    public User show(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            UserRepository repo = new UserRepository(con);
            return repo.find(id); // User entidad
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}