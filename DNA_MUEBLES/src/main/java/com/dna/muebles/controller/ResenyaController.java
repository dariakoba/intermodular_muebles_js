package com.dna.muebles.controller;

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

import com.dna.muebles.dto.ImagenResponse;
import com.dna.muebles.entity.Resenya;
import com.dna.muebles.exception.DataAccessException;
import com.dna.muebles.mapper.ResenyaMapper;
import com.dna.muebles.repository.ResenaImagenRepository;
import com.dna.muebles.repository.ResenyaRepository;

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
                    return ResponseEntity.ok(new ResenyaMapper().mapRow(rs));
                }
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
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

        String getSql = """
            SELECT id_usuario
            FROM resenas
            WHERE id_resena = ?
        """;

        String deleteSql = """
            DELETE FROM resenas
            WHERE id_resena = ?
            AND id_usuario = ?
        """;

        String puntosSql = """
        	    UPDATE usuarios
        	    SET puntos = GREATEST(puntos - 20, 0)
        	    WHERE id = ?
        	""";

        try (Connection con = ds.getConnection()) {

            int ownerId;

            // 1. obtener dueño de la reseña
            try (PreparedStatement ps = con.prepareStatement(getSql)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    return ResponseEntity.notFound().build();
                }

                ownerId = rs.getInt("id_usuario");
            }

            // 2. seguridad: solo el dueño puede borrar
            if (ownerId != usuarioId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("No puedes eliminar esta reseña");
            }

            // 3. restar puntos
            try (PreparedStatement ps = con.prepareStatement(puntosSql)) {
                ps.setInt(1, usuarioId);
                ps.executeUpdate();
            }

            // 4. borrar reseña
            try (PreparedStatement ps = con.prepareStatement(deleteSql)) {
                ps.setInt(1, id);
                ps.setInt(2, usuarioId);

                ps.executeUpdate();
            }

            return ResponseEntity.ok().build();

        } catch (SQLException e) {
            return ResponseEntity.internalServerError()
                    .body(e.getMessage());
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
            JOIN detalles_pedidos dp 
                ON dp.id_pedido = p.id_pedido
            WHERE p.id_usuario = ?
            AND dp.id_producto = ?
            AND p.estado_pago = 'Recibido'
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Sesión no iniciada");
        }

        try (Connection con = ds.getConnection()) {

            boolean comprado =
                haCompradoProducto(con, userId, resenya.getProductoId());

            if (!comprado) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Solo puedes reseñar productos que hayas recibido.");
            }

            
            String checkSql = """
                SELECT 1
                FROM resenas
                WHERE id_usuario = ?
                AND id_producto = ?
                LIMIT 1
            """;

            try (PreparedStatement check = con.prepareStatement(checkSql)) {

                check.setInt(1, userId);
                check.setInt(2, resenya.getProductoId());

                try (ResultSet rs = check.executeQuery()) {

                    if (rs.next()) {
                        return ResponseEntity
                            .badRequest()
                            .body("Ya has publicado una reseña para este producto.");
                    }
                }
            }

            resenya.setUsuarioId(userId);

            ResenyaRepository repo =
                new ResenyaRepository(con, new ResenyaMapper());

            Resenya guardada = repo.insert(resenya);

            

            int puntosGanados = 20;

            String puntosSql = """
                UPDATE usuarios
                SET puntos = puntos + ?
                WHERE id = ?
            """;

            try (PreparedStatement ps =
                    con.prepareStatement(puntosSql)) {

                ps.setInt(1, puntosGanados);
                ps.setInt(2, userId);

                ps.executeUpdate();
            }

            return ResponseEntity.ok(guardada);

        } catch (SQLException e) {

            e.printStackTrace();

            return ResponseEntity
                .internalServerError()
                .body("Error en BD: " + e.getMessage());
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

            Path uploadDir = Paths.get("uploads", folder);
            Path path = uploadDir.resolve(filename);

            Files.createDirectories(uploadDir);

            Files.write(path, file.getBytes());

            String url = "/uploads/" + folder + "/" + filename;

            return ResponseEntity.ok(Map.of("url", url));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Error subiendo archivo: " + e.getMessage());
        }
    }
}