package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.*;

import com.example.peliculas.entity.Pelicula;
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
    public List<Pelicula> index() throws SQLException {
    	try (Connection con = ds.getConnection()) {
    	    PeliculaRepository repo = new PeliculaRepository(con);
    	    return repo.findAll();
    	 } catch (SQLException e) {
    	        throw new DataAccessException(e);
    	 }
    }
    
    @GetMapping("/{id}")
    public Pelicula show(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            PeliculaRepository repo = new PeliculaRepository(con);
            return repo.find(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @PostMapping
    public Pelicula store(@RequestBody Pelicula pelicula) {
        try (Connection con = ds.getConnection()) {
            PeliculaRepository repo = new PeliculaRepository(con);
            repo.insert(pelicula);
            return pelicula;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @PutMapping("/{id}")
    public Pelicula update(@PathVariable int id, @RequestBody Pelicula pelicula) {
    	System.out.println(pelicula);
        try (Connection con = ds.getConnection()) {
            PeliculaRepository repo = new PeliculaRepository(con);
            pelicula.setId(id);
            repo.update(pelicula);
            return pelicula;
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
