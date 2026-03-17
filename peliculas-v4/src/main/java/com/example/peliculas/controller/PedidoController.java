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
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final DataSource ds;

    public PedidoController(DataSource ds) {
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

    @GetMapping("/{id}")
    public Pedido show(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            PedidoRepository repo = new PedidoRepository(con);
            return repo.find(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}