package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.peliculas.dto.PeliculaRequest;
import com.example.peliculas.entity.Pelicula;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.exception.NotFoundException;
import com.example.peliculas.repository.PeliculaRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/peliculas")
public class PeliculaAdminController {
	private final DataSource ds;

    public PeliculaAdminController(DataSource ds) {
    	this.ds = ds;
    }
    
    @GetMapping
    public List<Pelicula> index() {
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
            return repo.findOrThrow(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pelicula store(@Valid @RequestBody PeliculaRequest req) {
        try (Connection con = ds.getConnection()) {
            PeliculaRepository repo = new PeliculaRepository(con);
            Pelicula pelicula = map(req);
            return repo.insert(pelicula);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @PutMapping("/{id}")
    public Pelicula update(@PathVariable int id, @Valid @RequestBody PeliculaRequest req) {
        try (Connection con = ds.getConnection()) {
        	
            PeliculaRepository repo = new PeliculaRepository(con);
            Pelicula pelicula = map(req);
            pelicula.setId(id);
            
            if (repo.update(pelicula) == 0) {
            	throw new NotFoundException();
            }
            
            return pelicula;
            
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @DeleteMapping("/{id}")
    public void destroy(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            PeliculaRepository repo = new PeliculaRepository(con);
            if (repo.delete(id) == 0) {
            	throw new NotFoundException();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
    
    private Pelicula map(PeliculaRequest req) {
    	return new Pelicula(
    		null,
    		req.titulo(),
    		req.anyo(),
    		req.duracion(),
    		req.sinopsis(),
    		req.directorId()
    	);
    }
}
