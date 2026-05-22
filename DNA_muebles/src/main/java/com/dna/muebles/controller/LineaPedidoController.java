package com.dna.muebles.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.web.bind.annotation.*;

import com.dna.muebles.entity.LineaPedido;
import com.dna.muebles.exception.DataAccessException;
import com.dna.muebles.repository.LineaPedidoRepository;

@RestController
@RequestMapping("/api/lineas-pedido")
public class LineaPedidoController {

    private final DataSource ds;

    public LineaPedidoController(DataSource ds) {
        this.ds = ds;
    }

    @GetMapping
    public List<LineaPedido> index() {
        try (Connection con = ds.getConnection()) {
            LineaPedidoRepository repo = new LineaPedidoRepository(con);
            return repo.findAll();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
    
    // Ejemplo para insertar una nueva línea
    @PostMapping
    public void create(@RequestBody LineaPedido lp) {
        try (Connection con = ds.getConnection()) {
            LineaPedidoRepository repo = new LineaPedidoRepository(con);
            repo.insert(lp);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}