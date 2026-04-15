package com.example.peliculas.controller;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.web.bind.annotation.*;

import com.example.peliculas.dto.ProductoCatNomDetalle;
import com.example.peliculas.entity.Categoria;
import com.example.peliculas.entity.Producto;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.repository.CategoriaRepository;
import com.example.peliculas.repository.ProductoRepository;

@RestController
@RequestMapping("/api/admin/productos")
public class ProductoAdminController {

    private final DataSource ds;

    public ProductoAdminController(DataSource ds) {
        this.ds = ds;
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