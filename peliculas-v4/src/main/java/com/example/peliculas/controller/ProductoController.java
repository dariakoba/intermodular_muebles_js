package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.*;

import com.example.peliculas.dto.ProductoDetalle;
import com.example.peliculas.dto.ProductoResumen;
import com.example.peliculas.entity.Producto;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.repository.CategoriaRepository;
import com.example.peliculas.repository.ProductoRepository;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
	private final DataSource ds;
    public ProductoController(DataSource ds) {
        this.ds = ds;
    }
    
    @GetMapping
    public List<ProductoResumen> index() {
        try (Connection con = ds.getConnection()) {
        	ProductoRepository repo = new ProductoRepository(con);
            return repo.findResumen();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
    @GetMapping("/{id}")
    public Producto show(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
        	ProductoRepository repo = new ProductoRepository(con);
            return repo.find(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
