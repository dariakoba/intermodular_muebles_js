package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.web.bind.annotation.*;

import com.example.peliculas.dto.CarritoRequest;
import com.example.peliculas.entity.*;
import com.example.peliculas.repository.*;
import com.example.peliculas.exception.DataAccessException;

@RestController
@RequestMapping("/api/carrito")
public class PedidoController {

    private final DataSource ds;

    public PedidoController(DataSource ds) {
        this.ds = ds;
    }

    @PostMapping("/comprar")
    public String realizarCompra(@RequestBody CarritoRequest request) {
        try (Connection con = ds.getConnection()) {
            PedidoRepository pedidoRepo = new PedidoRepository(con);
            LineaPedidoRepository lineaRepo = new LineaPedidoRepository(con);

            Pedido nuevoPedido = pedidoRepo.insert(request.pedido);
            int idGenerado = nuevoPedido.getId();

            for (Integer idEjemplar : request.idsEjemplares) {
                LineaPedido linea = new LineaPedido(idGenerado, idEjemplar);
                lineaRepo.create(linea); 
            }

            return "Compra realizada con éxito. Pedido nº: " + idGenerado;

        } catch (SQLException e) {
            throw new DataAccessException("Error al procesar la compra", e);
        }
    }
    
    @GetMapping("/{id}")
    public Pedido verPedido(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            PedidoRepository repo = new PedidoRepository(con);
            return repo.find(id); 
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}