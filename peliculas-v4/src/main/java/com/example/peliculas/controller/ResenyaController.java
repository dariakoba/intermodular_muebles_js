package com.example.peliculas.controller;

import java.sql.*;
import java.time.LocalDate;
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
@RequestMapping("/api/resenyas")
public class ResenyaController {

    private final DataSource ds;

    public ResenyaController(DataSource ds) {
        this.ds = ds;
    }

    @GetMapping("/producto/{id}")
    public List<Resenya> getByProducto(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            ResenyaRepository repo = new ResenyaRepository(con, new ResenyaMapper());
            return repo.findByProducto(id);
        } catch (SQLException e) {
            throw new DataAccessException("Error al obtener reseñas", e);
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Resenya resenya, HttpSession session) {
        // 1. Validar sesión
        Integer userId = (Integer) session.getAttribute("userId");
        
     // DEPURACIÓN: Mira tu consola de Java al enviar la reseña
        System.out.println("ID Usuario Sesión: " + userId);
        System.out.println("ID Producto Recibido: " + resenya.getProductoId());
        
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"message\": \"Debe iniciar sesión para dejar una reseña.\"}");
        }

        try (Connection con = ds.getConnection()) {
            // 2. Comprobar si el usuario ha comprado el producto (Verificación de compra)
            // Usamos id_usuario e id_producto que son los nombres en tu tabla 'pedidos'
            String sqlCheck = "SELECT COUNT(*) FROM pedidos WHERE id_usuario = ? AND id_producto = ? AND activo = 1";
            
            try (PreparedStatement ps = con.prepareStatement(sqlCheck)) {
                ps.setInt(1, userId);
                ps.setInt(2, resenya.getProductoId()); // Ahora coincide con tu entidad
                
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body("{\"message\": \"Solo puedes reseñar muebles que hayas comprado.\"}");
                    }
                }
            }

            // 3. Si la validación es correcta, insertar
            ResenyaRepository repo = new ResenyaRepository(con, new ResenyaMapper());
            
            // Seteamos los datos que faltan desde el servidor por seguridad
            resenya.setUsuarioId(userId);
            resenya.setFechaPublicacion(LocalDate.now());
            
            Resenya nueva = repo.insert(resenya); 
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);

        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Error al procesar la reseña en la base de datos.\"}");
        }
    }
}