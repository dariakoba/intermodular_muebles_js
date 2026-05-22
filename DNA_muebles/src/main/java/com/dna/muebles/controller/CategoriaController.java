package com.dna.muebles.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.*;

import com.dna.muebles.dto.CategoriaDetalle;
import com.dna.muebles.exception.DataAccessException;
import com.dna.muebles.repository.CategoriaRepository;

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