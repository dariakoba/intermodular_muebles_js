package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.*;

import com.example.peliculas.entity.Categoria;
import com.example.peliculas.entity.Producto;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.repository.CategoriaRepository;
import com.example.peliculas.repository.ProductoRepository;

@RestController
@RequestMapping("/api/admin/categorias")
public class CategoriaAdminController {
	private final DataSource ds;
	
    public CategoriaAdminController(DataSource ds) {
        this.ds = ds;
    }
    
    @GetMapping
    public List<Categoria> index() {
        try (Connection con = ds.getConnection()) {
        	CategoriaRepository repo = new CategoriaRepository(con);
            return repo.findAll();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
    
    @PutMapping("/{id}/desactivar")
    public void desactivar(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
        	CategoriaRepository repo = new CategoriaRepository(con);
            repo.softDelete(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
