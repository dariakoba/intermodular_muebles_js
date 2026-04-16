package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

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

    @PostMapping("/comprar")
    public ResponseEntity<?> realizarCompra(@RequestBody CarritoRequest request) {
        // 1. Validación de seguridad: ¿Llega el pedido?
        if (request == null || request.pedido == null) {
            return ResponseEntity.badRequest().body("{\"message\": \"El pedido está vacío\"}");
        }

        try (Connection con = ds.getConnection()) {
            PedidoRepository pedidoRepo = new PedidoRepository(con);
            
            // 2. Limpieza de ID para evitar el error de "Clave duplicada"
            request.pedido.setIdPedido(null);
            
            // 3. Verificación de los campos que llegaban como NULL en tu log
            if (request.pedido.getClienteNombre() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                     .body("{\"message\": \"Error: El nombre del cliente no ha llegado al servidor\"}");
            }

            // 4. Inserción
            Pedido nuevoPedido = pedidoRepo.insert(request.pedido);
            
            // 5. Respuesta en formato JSON puro para que el JS no falle al leerla
            return ResponseEntity.ok("{\"message\": \"Compra realizada\", \"id\": " + nuevoPedido.getIdPedido() + "}");

        } catch (Exception e) {
            e.printStackTrace();
            // Si el error es de base de datos, mandamos un 409
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("{\"message\": \"Error SQL: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/admin/todos")
    public List<Pedido> listarTodosLosPedidos() {
        try (Connection con = ds.getConnection()) {
            PedidoRepository repo = new PedidoRepository(con);
            return repo.findAll();
        } catch (SQLException e) {
            throw new DataAccessException("Error al listar", e);
        }
    }

    @PutMapping("/{id}")
    public Pedido update(@PathVariable int id, @RequestBody Pedido pedido) {
        try (Connection con = ds.getConnection()) {
            PedidoRepository repo = new PedidoRepository(con);
            pedido.setIdPedido(id); 
            repo.update(pedido);
            return pedido;
        } catch (SQLException e) {
            throw new DataAccessException("Error al actualizar", e);
        }
    }

    @GetMapping("/{id}")
    public Pedido verPedido(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            PedidoRepository repo = new PedidoRepository(con);
            return repo.find(id); 
        } catch (SQLException e) {
            throw new DataAccessException("No encontrado", e);
        }
    }
}