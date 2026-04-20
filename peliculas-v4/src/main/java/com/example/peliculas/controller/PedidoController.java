package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.peliculas.db.DB;
import com.example.peliculas.dto.CarritoRequest;
import com.example.peliculas.entity.Pedido;
import com.example.peliculas.repository.PedidoRepository;
import com.example.peliculas.exception.DataAccessException;


@RestController
@RequestMapping("/api/carrito")
public class PedidoController {

    private final DataSource ds;

    public PedidoController(DataSource ds) {
        this.ds = ds;
    }

    // --- SECCIÓN CLIENTE: REALIZAR COMPRA ---
    
    @PostMapping("/comprar")
    public ResponseEntity<?> realizarCompra(@RequestBody CarritoRequest request) {
        if (request == null || request.pedido == null) {
            return ResponseEntity.badRequest().body("{\"message\": \"El pedido está vacío\"}");
        }

        try (Connection con = ds.getConnection()) {
            PedidoRepository pedidoRepo = new PedidoRepository(con);
            
            // Forzamos ID null para evitar error de clave duplicada
            request.pedido.setIdPedido(null);
            
            if (request.pedido.getClienteNombre() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                     .body("{\"message\": \"Error: El nombre del cliente es nulo\"}");
            }

            Pedido nuevoPedido = pedidoRepo.insert(request.pedido);
            return ResponseEntity.ok("{\"message\": \"Compra realizada\", \"id\": " + nuevoPedido.getIdPedido() + "}");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("{\"message\": \"Error SQL: " + e.getMessage() + "\"}");
        }
    }

    // --- SECCIÓN ADMIN: GESTIÓN DE PEDIDOS (Rutas desbloqueadas) ---

    @GetMapping("/admin/lista")
    public List<Pedido> listarParaAdmin() {
        try (Connection con = ds.getConnection()) {
            PedidoRepository repo = new PedidoRepository(con);
            return repo.findAll(); // Asegúrate de que findAll() tenga el ORDER BY id_pedido DESC
        } catch (SQLException e) {
            throw new DataAccessException("Error al listar pedidos", e);
        }
    }

    @DeleteMapping("/admin/eliminar/{id}")
    public ResponseEntity<?> borrar(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            PedidoRepository repo = new PedidoRepository(con);
            repo.delete(id);
            return ResponseEntity.ok("{\"message\": \"Pedido eliminado\"}");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Error al borrar\"}");
        }
    }


    @GetMapping("/admin/usuarios")
    public List<Map<String, Object>> listarUsuariosCompañera() {
        String sql = "SELECT id, nombre, apellidos, email, rol, estado FROM usuarios";
        try (Connection con = ds.getConnection()) {
            return DB.queryMany(con, sql, (rs) -> {
                Map<String, Object> u = new HashMap<>();
                u.put("id", rs.getInt("id"));
                u.put("nombre", rs.getString("nombre") + " " + rs.getString("apellidos"));
                u.put("email", rs.getString("email"));
                u.put("rol", rs.getString("rol"));
                u.put("estado", rs.getString("estado"));
                return u;
            });
        } catch (SQLException e) {
            throw new DataAccessException("Error al acceder a la tabla de usuarios", e);
        }
    }


    @GetMapping("/{id}")
    public Pedido verPedido(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            return new PedidoRepository(con).find(id); 
        } catch (SQLException e) {
            throw new DataAccessException("No encontrado", e);
        }
    }
}