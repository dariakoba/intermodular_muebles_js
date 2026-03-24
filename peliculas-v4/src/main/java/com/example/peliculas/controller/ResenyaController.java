package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.web.bind.annotation.*;
import com.example.peliculas.entity.Resenya;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.repository.ResenyaRepository;

@RestController
@RequestMapping("/api/resenyas")
public class ResenyaController {

    private final DataSource ds;

    public ResenyaController(DataSource ds) {
        this.ds = ds;
    }

    @GetMapping("/producto/{id}")
    public List<Resenya> getByProducto(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            ResenyaRepository repo = new ResenyaRepository(con);
            return repo.findByProducto(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @PostMapping
    public Resenya create(@RequestBody Resenya resenya) {
        try (Connection con = ds.getConnection()) {
            ResenyaRepository repo = new ResenyaRepository(con);
            int id = repo.insert(resenya);
            resenya.setIdResenya(id);
            return resenya;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
