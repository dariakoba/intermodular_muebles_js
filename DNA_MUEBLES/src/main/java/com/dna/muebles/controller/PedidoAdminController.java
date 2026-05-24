package com.dna.muebles.controller;

import java.sql.Connection;

import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.web.bind.annotation.*;

import com.dna.muebles.entity.Pedido;
import com.dna.muebles.exception.DataAccessException;
import com.dna.muebles.repository.PedidoRepository;

@RestController
@RequestMapping("/api/admin/pedidos")
public class PedidoAdminController {

    private final DataSource ds;

    public PedidoAdminController(DataSource ds) {
        this.ds = ds;
    }

    // Devuelve todos los pedidos activos
    @GetMapping
    public List<Pedido> index() {
        try (Connection con = ds.getConnection()) {
            PedidoRepository repo = new PedidoRepository(con);
            return repo.findAll();
        } catch (SQLException e) {
            throw new DataAccessException("Error en el listado admin", e);
        }
    }

    // (Soft Delete)
    @DeleteMapping("/{id}")
    public void destroy(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            PedidoRepository repo = new PedidoRepository(con);
            repo.delete(id);
        } catch (SQLException e) {
            throw new DataAccessException("Error al eliminar pedido", e);
        }
    }
}