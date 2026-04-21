package com.example.peliculas.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.peliculas.dto.UserResponse;
import com.example.peliculas.entity.User;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

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
    
    @PutMapping("/update-me")
    public UserResponse updateMe(@RequestBody User data, HttpSession session) {
        // Extraemos el ID de la sesión
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sesión no iniciada");
        }

        try (Connection con = ds.getConnection()) {
            UserRepository repo = new UserRepository(con);
            
            // 1. Buscamos el usuario actual en la base de datos
            User user = repo.find(userId);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
            }

            // 2. Validamos que no vengan campos vacíos
            if (data.getNombre() == null || data.getNombre().isBlank() ||
                data.getEmail() == null || data.getEmail().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre y Email son obligatorios");
            }

            // 3. Actualizamos solo los campos permitidos
            user.setNombre(data.getNombre());
            user.setApellidos(data.getApellidos()); // Asegúrate de que User tenga este campo
            user.setEmail(data.getEmail());
            user.setTelefono(data.getTelefono());
            user.setDireccion(data.getDireccion());

            // 4. Guardamos en la base de datos
            repo.update(user);
            
            // 5. Devolvemos el DTO actualizado (sin contraseña)
            return repo.findResponseById(userId);

        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
    
    @PostMapping("/upload-photo")
    public UserResponse uploadPhoto(@RequestParam("file") MultipartFile file, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        try (Connection con = ds.getConnection()) {
            UserRepository repo = new UserRepository(con);
            User user = repo.find(userId);

            // 1. Definimos la ruta relativa a la carpeta static
            String fileName = "user_" + userId + ".jpg";
            // Usamos una ruta que apunte a tu nueva carpeta
            Path root = Paths.get("src/main/resources/static/images/fotoperfil");
            
            // Creamos la carpeta si por algún motivo no existe
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }

            // 2. Guardamos el archivo
            Files.copy(file.getInputStream(), root.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

            // 3. La URL que guardamos en BD debe ser la que el navegador usará:
            user.setFotoUrl("/images/fotoperfil/" + fileName);
            repo.update(user);

            return repo.findResponseById(userId);
        } catch (Exception e) {
            e.printStackTrace(); 
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar la imagen");
        }
    }
}