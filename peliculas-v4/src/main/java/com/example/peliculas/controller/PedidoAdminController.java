package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.web.bind.annotation.*;
import com.example.peliculas.entity.Pedido;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.repository.PedidoRepository;

@RestController
@RequestMapping("/api/admin/pedidos")
public class PedidoAdminController {

    private final DataSource ds;

    public PedidoAdminController(DataSource ds) {
        this.ds = ds;
    }

    @GetMapping
    public List<Pedido> index() {
        try (Connection con = ds.getConnection()) {
            PedidoRepository repo = new PedidoRepository(con);
            return repo.findAll();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @PostMapping
    public Pedido store(@RequestBody Pedido pedido) {
        try (Connection con = ds.getConnection()) {
            PedidoRepository repo = new PedidoRepository(con);
            repo.insert(pedido);
            return pedido;
        } catch (SQLException e) {
            throw new DataAccessException(e);
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
            throw new DataAccessException(e);
        }
    }

    @DeleteMapping("/{id}")
    public void destroy(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            PedidoRepository repo = new PedidoRepository(con);
            repo.delete(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
    
 // Añade este método a PedidoController.java

    @GetMapping("/admin/todos")
    public List<Pedido> listarTodosLosPedidos() {
        try (Connection con = ds.getConnection()) {
            PedidoRepository repo = new PedidoRepository(con);
            return repo.findAll(); 
        } catch (SQLException e) {
            throw new DataAccessException("Error de acceso al panel de administración", e);
        }
    }
}