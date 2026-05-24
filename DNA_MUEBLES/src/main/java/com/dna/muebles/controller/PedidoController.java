package com.dna.muebles.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpSession;

import com.dna.muebles.db.DB;
import com.dna.muebles.dto.CarritoRequest;
import com.dna.muebles.entity.Pedido;
import com.dna.muebles.exception.DataAccessException;
import com.dna.muebles.repository.PedidoRepository;
import com.dna.muebles.repository.UserRepository;
import com.dna.muebles.service.PedidoService;

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

        // Cálculos de importes y puntos
        float totalOriginal = 0f;
        for (Map<String, Object> item : request.getProductos()) {
            float precio = Float.parseFloat(item.get("precio").toString());
            int cant = Integer.parseInt(item.get("cantidad").toString());
            totalOriginal += (precio * cant);
        }

        float totalPagado = request.getPedido().getTotal();
        int puntosAUsar = 0;
        if (totalOriginal > totalPagado + 0.05f) { 
            float descuento = totalOriginal - totalPagado;
            puntosAUsar = Math.round(descuento * 100);
        }
        
        // SOLO DAMOS PUNTOS INICIALES SI ESTÁ PAGADO CON TARJETA
        int puntosGanados = 0;
        if ("Pagado".equalsIgnoreCase(request.getPedido().getEstadoPago())) {
            puntosGanados = (int) (totalPagado * 5);
        }

        try {
            PedidoService pedidoService = new PedidoService(this.ds);
            pedidoService.realizarCompra(userId, request, puntosAUsar, puntosGanados);
            return ResponseEntity.ok("{\"message\": \"Compra realizada con éxito\"}");
        } catch (RuntimeException e) {
            System.err.println("Compra denegada: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\": \"" + e.getMessage() + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Error inesperado al procesar el pedido.\"}");
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
            
            // Lógica de puntos 
            Pedido pedidoAnterior = repo.find(id);
            if (!"Pagado".equalsIgnoreCase(pedidoAnterior.getEstadoPago()) && "Pagado".equalsIgnoreCase(nuevoEstado)) {
                int puntosGanados = (int) (pedidoAnterior.getTotal() * 5);
                UserRepository userRepo = new UserRepository(con);
                userRepo.actualizarPuntos(pedidoAnterior.getIdUsuario(), 0, puntosGanados);
            }

            repo.actualizarEstado(id, nuevoEstado);
            return ResponseEntity.ok("{\"message\": \"Estado actualizado\"}");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Error al cambiar estado\"}");
        }
    }

    @PutMapping("/admin/editar/{id}")
    public ResponseEntity<?> editarPedidoCompleto(@PathVariable int id, @RequestBody Map<String, String> payload) {
        try (Connection con = ds.getConnection()) {
            PedidoRepository repo = new PedidoRepository(con);
            
            String estadoNuevo = payload.get("estado");
            String clienteNombre = payload.get("clienteNombre");
            String fecha = payload.get("fecha");
            String email = payload.get("email");
            String telefono = payload.get("telefono");
            String direccion = payload.get("direccion");
            
            // Lógica de puntos diferidos en Modal Edición
            Pedido pedidoAnterior = repo.find(id);
            if (!"Pagado".equalsIgnoreCase(pedidoAnterior.getEstadoPago()) && "Pagado".equalsIgnoreCase(estadoNuevo)) {
                int puntosGanados = (int) (pedidoAnterior.getTotal() * 5);
                UserRepository userRepo = new UserRepository(con);
                userRepo.actualizarPuntos(pedidoAnterior.getIdUsuario(), 0, puntosGanados);
            }
            
            repo.actualizarPedidoCompleto(id, estadoNuevo, clienteNombre, fecha, email, telefono, direccion);
            
            return ResponseEntity.ok("{\"message\": \"Pedido actualizado completamente\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"message\": \"Error al editar pedido\"}");
        }
    }

    @GetMapping("/admin/usuarios")
    public List<Map<String, Object>> listarUsuarios() {
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
    public List<Pedido> misPedidos(HttpSession session) throws SQLException {
        Integer userId = (Integer) session.getAttribute("userId");
        try (Connection con = ds.getConnection()) {
            PedidoRepository pedidoRepo = new PedidoRepository(con);
            return pedidoRepo.findByUsuarioId(userId);
        }
    }
}