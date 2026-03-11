package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.*;

import com.example.peliculas.dto.PeliculaDetalle;
import com.example.peliculas.dto.PeliculaResumen;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.repository.PeliculaRepository;

@RestController
@RequestMapping("/api/peliculas")
public class PeliculaController {
	private final DataSource ds;
    public PeliculaController(DataSource ds) {
        this.ds = ds;
    }
    
    @GetMapping
    public List<PeliculaResumen> index() {
        try (Connection con = ds.getConnection()) {
            PeliculaRepository repo = new PeliculaRepository(con);
            return repo.findResumen();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
    @GetMapping("/{id}")
    public PeliculaDetalle show(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            PeliculaRepository repo = new PeliculaRepository(con);
            return repo.findDetalle(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
