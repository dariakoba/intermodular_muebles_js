package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.peliculas.dto.ProductoCatNomDetalle;
import com.example.peliculas.entity.Producto;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.repository.ProductoRepository;

import com.example.peliculas.helper.StorageHelper;
import com.example.peliculas.validation.ImageValidator;


@RestController
@RequestMapping("/api/admin/productos/{productoId}/imagenes")
public class ProductoImagenController {
	 private final DataSource ds;
	 private final StorageHelper storage;

	  public ProductoImagenController(DataSource ds) {
	        this.ds = ds;
			this.storage = storage;

	    }
	    
	    @GetMapping
	    public List<ProductoCatNomDetalle> index() throws SQLException {
	        try (Connection con = ds.getConnection()) {
	            ProductoRepository repo = new ProductoRepository(con);
	            return repo.findDetalleCategoria();
	        } catch (SQLException e) {
	            throw new DataAccessException(e);
	        }
	    }
	    
	    @PutMapping("/{id}/desactivar")
	    public void desactivar(@PathVariable int id) {
	        try (Connection con = ds.getConnection()) {
	            ProductoRepository repo = new ProductoRepository(con);
	            repo.softDelete(id);
	        } catch (SQLException e) {
	            throw new DataAccessException(e);
	        }
	    }
	    
	    @PutMapping("/{id}/activar")
	    public void activar(@PathVariable int id) {
	        try (Connection con = ds.getConnection()) {
	            ProductoRepository repo = new ProductoRepository(con);
	            repo.softDeleteActivar(id);
	        } catch (SQLException e) {
	            throw new DataAccessException(e);
	        }
	    }
	    /*
	     * antiguo sin nombre categoria
	    @GetMapping
	    public List<Producto> index() throws SQLException {
	        try (Connection con = ds.getConnection()) {
	            ProductoRepository repo = new ProductoRepository(con);
	            return repo.findAll();
	        } catch (SQLException e) {
	            throw new DataAccessException(e);
	        }
	    }
		*/
		
	    @GetMapping("/{id}")
	    public Producto show(@PathVariable int id) {
	        try (Connection con = ds.getConnection()) {
	            ProductoRepository repo = new ProductoRepository(con);
	            return repo.find(id);
	        } catch (SQLException e) {
	            throw new DataAccessException(e);
	        }
	    }

	    @PostMapping
	    public Producto store(@RequestBody Producto producto) {
	        try (Connection con = ds.getConnection()) {
	            ProductoRepository repo = new ProductoRepository(con);
	            repo.insert(producto);
	            return producto;
	        } catch (SQLException e) {
	            throw new DataAccessException(e);
	        }
	    }

	    @PutMapping("/{id}")
	    public Producto update(@PathVariable int id, @RequestBody Producto producto) {
	        System.out.println(producto);
	        try (Connection con = ds.getConnection()) {
	            ProductoRepository repo = new ProductoRepository(con);
	            producto.setIdProducto(id);
	            repo.update(producto);
	            return producto;
	        } catch (SQLException e) {
	            throw new DataAccessException(e);
	        }
	    }

	    @DeleteMapping("/{id}")
	    public void destroy(@PathVariable int id) {
	        try (Connection con = ds.getConnection()) {
	            ProductoRepository repo = new ProductoRepository(con);
	            repo.delete(id);
	        } catch (SQLException e) {
	            throw new DataAccessException(e);
	        }
	    }
}
