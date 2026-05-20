package com.example.peliculas.controller;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.peliculas.dto.ImagenResponse;
import com.example.peliculas.entity.Resenya;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.repository.ResenaImagenRepository;
import com.example.peliculas.repository.ResenyaRepository;
import com.example.peliculas.mapper.ResenyaMapper;

import jakarta.servlet.http.HttpSession;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    
    @PostMapping("/resenyas/{id}/imagenes")
    public ResponseEntity<?> guardarImagen(
            @PathVariable int id,
            @RequestBody ImagenResponse body
    ) {

        String sql = """
            INSERT INTO resena_imagenes(resena_id, url)
            VALUES (?, ?)
        """;

        try (
            Connection con = ds.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setInt(1, id);
            ps.setString(2, body.url());

            ps.executeUpdate();

            return ResponseEntity.ok().build();

        } catch (SQLException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    
    @GetMapping("/resenyas/{id}/imagenes")
    public ResponseEntity<?> getImagenes(@PathVariable int id) {

        try (Connection con = ds.getConnection()) {

            ResenaImagenRepository repo =
                new ResenaImagenRepository(con);

            return ResponseEntity.ok(
                repo.findByResenaId(id)
            );

        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                .body(e.getMessage());
        }
    }
     
    private boolean haCompradoProducto(Connection con, int userId, int productoId) throws SQLException {

        String sql = """
            SELECT 1
            FROM pedidos p
            JOIN detalles_pedidos dp ON dp.id_pedido = p.id_pedido
            WHERE p.id_usuario = ?
            AND dp.id_producto = ?
            LIMIT 1
        """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productoId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    @PostMapping("/resenyas")
    public ResponseEntity<?> crearResenya(@RequestBody Resenya resenya, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sesión no iniciada");
        }

        try (Connection con = ds.getConnection()) {
            // Log para depurar (míralo en la consola de tu IDE)
            System.out.println("Validando compra - Usuario: " + userId + " Producto: " + resenya.getProductoId());

            boolean comprado = haCompradoProducto(con, userId, resenya.getProductoId());

            if (!comprado) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Solo puedes reseñar productos que hayas comprado.");
            }

            // Insertar la reseña
            resenya.setUsuarioId(userId);
            ResenyaRepository repo = new ResenyaRepository(con, new ResenyaMapper());
            Resenya guardada = repo.insert(resenya);

            return ResponseEntity.ok(guardada);

        } catch (SQLException e) {
            e.printStackTrace(); // Esto te dirá el error exacto en la consola si vuelve a fallar
            return ResponseEntity.internalServerError().body("Error en BD: " + e.getMessage());
        }
    }
    
    @GetMapping("/resenyas/producto/{id}")
    public ResponseEntity<?> getByProducto(
            @PathVariable int id
    ) {

        try (Connection con = ds.getConnection()) {

            ResenyaRepository repo =
                new ResenyaRepository(
                    con,
                    new ResenyaMapper()
                );

            return ResponseEntity.ok(
                repo.findByProducto(id)
            );

        } catch (Exception e) {

            return ResponseEntity
                .internalServerError()
                .body(e.getMessage());
        }
    }
    
    @DeleteMapping("/resenyas/{id}")
    public ResponseEntity<?> deleteResenya(
            @PathVariable int id,
            HttpSession session
    ) {

        Integer usuarioId =
            (Integer) session.getAttribute("userId");

        if (usuarioId == null) {

            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("No autorizado");
        }

        String sql = """
            DELETE FROM resenas
            WHERE id_resena = ?
            AND id_usuario = ?
        """;

        try (
            Connection con = ds.getConnection();
            PreparedStatement ps =
                con.prepareStatement(sql)
        ) {

            ps.setInt(1, id);
            ps.setInt(2, usuarioId);

            int rows = ps.executeUpdate();

            if (rows == 0) {

                return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("No puedes eliminar esta reseña");
            }

            return ResponseEntity.ok().build();

        } catch (SQLException e) {

            return ResponseEntity
                .internalServerError()
                .body(e.getMessage());
        }
    }
    
    @PostMapping("/{folder}")
    public ResponseEntity<?> upload(
            @PathVariable String folder,
            @RequestParam("file") MultipartFile file
    ) {

        if (!List.of("usuarios", "productos", "resenyas").contains(folder)) {
            return ResponseEntity.badRequest().body("Carpeta no válida");
        }

        try {
            String original = file.getOriginalFilename();

            if (original == null || !original.contains(".")) {
                return ResponseEntity.badRequest().body("Archivo inválido");
            }

            String extension = original.substring(original.lastIndexOf("."));
            String filename = UUID.randomUUID() + extension;

            // 🔥 CAMBIO IMPORTANTE: ruta estable dentro del proyecto
            Path uploadDir = Paths.get("uploads", folder);
            Path path = uploadDir.resolve(filename);

            // crear carpeta SIEMPRE
            Files.createDirectories(uploadDir);

            // guardar archivo
            Files.write(path, file.getBytes());

            String url = "/uploads/" + folder + "/" + filename;

            return ResponseEntity.ok(Map.of("url", url));

        } catch (Exception e) {
            e.printStackTrace(); // MUY IMPORTANTE
            return ResponseEntity.internalServerError()
                    .body("Error subiendo archivo: " + e.getMessage());
        }
    }
}