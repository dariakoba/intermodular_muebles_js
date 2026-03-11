package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.*;

import com.example.peliculas.entity.Categoria;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.repository.PeliculaRepository;

@RestController
@RequestMapping("/api/admin/peliculas")
public class PeliculaAdminController {
	private final DataSource ds;

    public PeliculaAdminController(DataSource ds) {
    	this.ds = ds;
    }
    
    @GetMapping
    public List<Categoria> index() throws SQLException {
    	try (Connection con = ds.getConnection()) {
    	    PeliculaRepository repo = new PeliculaRepository(con);
    	    return repo.findAll();
    	 } catch (SQLException e) {
    	        throw new DataAccessException(e);
    	 }
    }
    
    @GetMapping("/{id}")
    public Categoria show(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            PeliculaRepository repo = new PeliculaRepository(con);
            return repo.find(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @PostMapping
    public Categoria store(@RequestBody Categoria categoria) {
        try (Connection con = ds.getConnection()) {
            PeliculaRepository repo = new PeliculaRepository(con);
            repo.insert(categoria);
            return categoria;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @PutMapping("/{id}")
    public Categoria update(@PathVariable int id, @RequestBody Categoria categoria) {
    	System.out.println(categoria);
        try (Connection con = ds.getConnection()) {
            PeliculaRepository repo = new PeliculaRepository(con);
            categoria.setId(id);
            repo.update(categoria);
            return categoria;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @DeleteMapping("/{id}")
    public void destroy(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            PeliculaRepository repo = new PeliculaRepository(con);
            repo.delete(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
