package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.*;

import com.example.peliculas.dto.CategoriaDetalle;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.repository.CategoriaRepository;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final DataSource ds;

    public CategoriaController(DataSource ds) {
        this.ds = ds;
    }

    @GetMapping
    public List<CategoriaDetalle> index() {
        try (Connection con = ds.getConnection()) {
            CategoriaRepository repo = new CategoriaRepository(con);
            return repo.findAllCategoriasActivas();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}