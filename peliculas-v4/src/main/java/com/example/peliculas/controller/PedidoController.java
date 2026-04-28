package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.example.peliculas.db.DB;
import com.example.peliculas.dto.CarritoRequest;
import com.example.peliculas.entity.Pedido;
import com.example.peliculas.repository.PedidoRepository;
import com.example.peliculas.repository.UserRepository; 
import com.example.peliculas.exception.DataAccessException;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/carrito")
public class PedidoController {

    private final DataSource ds;

    public PedidoController(DataSource ds) {
        this.ds = ds;
    }

    @PostMapping("/comprar")
    public ResponseEntity<?> realizarCompra(@RequestBody CarritoRequest request, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"Inicia sesión para comprar\"}");
        }

        // =========================================================
        // 🔥 LA SOLUCIÓN DEFINITIVA: CÁLCULO EN EL SERVIDOR 🔥
        // =========================================================
        
        // 1. Calculamos cuánto valen los muebles originalmente (sin descuento)
        float totalOriginal = 0f;
        for (Map<String, Object> item : request.getProductos()) {
            float precio = Float.parseFloat(item.get("precio").toString());
            int cant = Integer.parseInt(item.get("cantidad").toString());
            totalOriginal += (precio * cant);
        }

        // 2. Vemos cuánto ha pagado realmente
        float totalPagado = request.getPedido().getTotal();

        // 3. Calculamos la diferencia matemáticamente (¡A prueba de fallos!)
        int puntosAUsar = 0;
        if (totalOriginal > totalPagado + 0.05f) { // El +0.05 es para ignorar céntimos sueltos
            float descuento = totalOriginal - totalPagado;
            puntosAUsar = Math.round(descuento * 100);
        }

        System.out.println(">>> VALOR ORIGINAL MUEBLES: " + totalOriginal + "€");
        System.out.println(">>> TOTAL PAGADO: " + totalPagado + "€");
        System.out.println(">>> PUNTOS CALCULADOS POR JAVA: " + puntosAUsar);
        // =========================================================

        try (Connection con = ds.getConnection()) {
            
            UserRepository userRepo = new UserRepository(con);
            
            if (request.getDireccion() != null && !request.getDireccion().isEmpty()) {
                userRepo.actualizarDireccion(userId, request.getDireccion());
            }

            int puntosGanados = (int) (totalPagado * 5);
            userRepo.actualizarPuntos(userId, puntosAUsar, puntosGanados);
            
            PedidoRepository pedidoRepo = new PedidoRepository(con);
            
            Pedido p = request.getPedido();
            p.setIdUsuario(userId);
            p.setFecha(LocalDate.now());
            
            // ¡LE INYECTAMOS EL CÁLCULO SEGURO AL PEDIDO!
            p.setPuntosUsados(puntosAUsar);

            Pedido nuevoPedido = pedidoRepo.insert(p);
            int idGenerado = nuevoPedido.getIdPedido();

            for (Map<String, Object> item : request.getProductos()) {
                int idProd = Integer.parseInt(item.get("id_producto").toString());
                int cant = Integer.parseInt(item.get("cantidad").toString());
                float precio = Float.parseFloat(item.get("precio").toString());
                
                pedidoRepo.guardarDetalle(idGenerado, idProd, cant, precio);
            }

            return ResponseEntity.ok("{\"message\": \"Compra realizada con éxito\"}");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"message\": \"Error al procesar: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/admin/lista")
    public List<Pedido> listarParaAdmin() {
        try (Connection con = ds.getConnection()) {
            PedidoRepository repo = new PedidoRepository(con);
            return repo.findAll(); 
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

    @PutMapping("/admin/estado/{id}")
    public ResponseEntity<?> cambiarEstado(@PathVariable int id, @RequestBody Map<String, String> body) {
        try (Connection con = ds.getConnection()) {
            PedidoRepository repo = new PedidoRepository(con);
            String nuevoEstado = body.get("estado");
            repo.actualizarEstado(id, nuevoEstado);
            return ResponseEntity.ok("{\"message\": \"Estado actualizado\"}");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Error al cambiar estado\"}");
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
            throw new DataAccessException("Error", e);
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
    
    @GetMapping("/mis")
    public List<Pedido> misPedidos(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Debes iniciar sesión");
        try (Connection con = ds.getConnection()) {
            PedidoRepository pedidoRepo = new PedidoRepository(con);
            return pedidoRepo.findByUsuarioId(userId);
        } catch (SQLException e) {
            throw new DataAccessException("Error", e);
        }
    }
}