package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.*;

import com.example.peliculas.dto.CategoriaDetalle;
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
    public List<CategoriaDetalle> index() {
        try (Connection con = ds.getConnection()) {
        	CategoriaRepository repo = new CategoriaRepository(con);
            return repo.findAllCategoria();
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
    
    @PutMapping("/{id}/activar")
    public void activar(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            CategoriaRepository repo = new CategoriaRepository(con);
            repo.softDeleteActivar(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
    
    @GetMapping("/{id}")
    public Categoria show(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            CategoriaRepository repo = new CategoriaRepository(con);
            return repo.find(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
    
    @PostMapping
    public Categoria store(@RequestBody Categoria categoria) {
        try (Connection con = ds.getConnection()) {
            CategoriaRepository repo = new CategoriaRepository(con);
            repo.insert(categoria);
            return categoria;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @PutMapping("/{id}")
    public Categoria update(@PathVariable int id, @RequestBody Categoria c) {
        System.out.println(c);
        try (Connection con = ds.getConnection()) {
            CategoriaRepository repo = new CategoriaRepository(con);
            c.setIdCategoria(id);
            repo.update(c);
            return c;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @DeleteMapping("/{id}")
    public void destroy(@PathVariable int id) {
        try (Connection con = ds.getConnection()) {
            CategoriaRepository repo = new CategoriaRepository(con);
            repo.delete(id);
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
}
