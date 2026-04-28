package com.example.peliculas.controller;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.peliculas.entity.Resenya;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.repository.ResenyaRepository;
import com.example.peliculas.mapper.ResenyaMapper;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class ResenyaController {

    private final DataSource ds;

    public ResenyaController(DataSource ds) {
        this.ds = ds;
    }

    // --- ENDPOINTS ADMIN ---

    @GetMapping("/admin/resenyas")
    public ResponseEntity<?> getAllAdmin() {
        // IMPORTANTE: fíjate en u.email AS email_usuario
        String sql = "SELECT r.*, " +
                     "u.nombre AS nombre_usuario, " +
                     "u.email AS email_usuario, " +
                     "p.nombre AS nombre_producto " +
                     "FROM resenas r " +
                     "LEFT JOIN usuarios u ON r.id_usuario = u.id " +
                     "LEFT JOIN productos p ON r.id_producto = p.id_producto " +
                     "ORDER BY r.fecha DESC";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Resenya> lista = new ArrayList<>();
            ResenyaMapper mapper = new ResenyaMapper();

            while (rs.next()) {
                lista.add(mapper.mapRow(rs));
            }
            return ResponseEntity.ok(lista);
        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/admin/resenyas/{id}")
    public ResponseEntity<?> getByIdAdmin(@PathVariable int id) {
        // CORRECCIÓN: Añadimos u.email AS email_usuario a la consulta
        String sql = "SELECT r.*, " +
                     "u.nombre AS nombre_usuario, " +
                     "u.email AS email_usuario, " +
                     "p.nombre AS nombre_producto " +
                     "FROM resenas r " +
                     "LEFT JOIN usuarios u ON r.id_usuario = u.id " +
                     "LEFT JOIN productos p ON r.id_producto = p.id_producto " +
                     "WHERE r.id_resena = ?";

        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // El Mapper ya sabe leer "email_usuario" si lo incluimos en el SQL
                    return ResponseEntity.ok(new ResenyaMapper().mapRow(rs));
                }
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/admin/resenyas/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable int id) {
        String sql = "DELETE FROM resenas WHERE id_resena = ?";
        try (Connection con = ds.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0 ? ResponseEntity.ok("{\"success\": true}") : ResponseEntity.notFound().build();
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}