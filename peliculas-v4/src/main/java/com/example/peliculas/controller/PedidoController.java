package com.example.peliculas.controller;

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

        float totalOriginal = 0f;
        for (Map<String, Object> item : request.getProductos()) {
            // Protección contra nulos al calcular el total
            Object precioObj = item.get("precio");
            Object cantObj = item.get("cantidad");
            
            float precio = (precioObj != null) ? Float.parseFloat(precioObj.toString()) : 0f;
            int cant = (cantObj != null) ? Integer.parseInt(cantObj.toString()) : 1;
            totalOriginal += (precio * cant);
        }

        float totalPagado = request.getPedido().getTotal();
        int puntosAUsar = 0;
        if (totalOriginal > totalPagado + 0.05f) { 
            float descuento = totalOriginal - totalPagado;
            puntosAUsar = Math.round(descuento * 100);
        }

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
            p.setPuntosUsados(puntosAUsar);

            Pedido nuevoPedido = pedidoRepo.insert(p);
            int idGenerado = nuevoPedido.getIdPedido();

            // Guardamos los detalles del pedido con seguridad
            for (Map<String, Object> item : request.getProductos()) {
                Object idProdObj = item.get("id_producto");
                if (idProdObj == null) {
                    idProdObj = item.get("id"); // Por si acaso desde JS se envía como 'id'
                }
                
                // Si seguimos sin tener ID, saltamos el producto para no romper el servidor
                if (idProdObj == null) {
                    continue; 
                }

                int idProd = Integer.parseInt(idProdObj.toString());
                
                Object cantObj = item.get("cantidad");
                Object precioObj = item.get("precio");
                int cant = (cantObj != null) ? Integer.parseInt(cantObj.toString()) : 1;
                float precio = (precioObj != null) ? Float.parseFloat(precioObj.toString()) : 0f;
                
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

    @PutMapping("/admin/editar/{id}")
    public ResponseEntity<?> editarPedidoCompleto(@PathVariable int id, @RequestBody Map<String, String> payload) {
        try (Connection con = ds.getConnection()) {
            PedidoRepository repo = new PedidoRepository(con);
            
            String estado = payload.get("estado");
            String clienteNombre = payload.get("clienteNombre");
            String fecha = payload.get("fecha");
            String email = payload.get("email");
            String telefono = payload.get("telefono");
            String direccion = payload.get("direccion");
            
            repo.actualizarPedidoCompleto(id, estado, clienteNombre, fecha, email, telefono, direccion);
            
            return ResponseEntity.ok("{\"message\": \"Pedido actualizado completamente\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("{\"message\": \"Error al editar pedido\"}");
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
    public List<Pedido> misPedidos(HttpSession session) throws SQLException {
        Integer userId = (Integer) session.getAttribute("userId");
        try (Connection con = ds.getConnection()) {
            PedidoRepository pedidoRepo = new PedidoRepository(con);
            return pedidoRepo.findByUsuarioId(userId);
        }
    }
}