package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.*;

import com.example.peliculas.dto.ProductoDetalle;
import com.example.peliculas.dto.ProductoResumen;
import com.example.peliculas.entity.Producto;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.repository.CategoriaRepository;
import com.example.peliculas.repository.ProductoRepository;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
	private final DataSource ds;
    public ProductoController(DataSource ds) {
        this.ds = ds;
    }
    
    /*
    @GetMapping
    public List<ProductoResumen> index() {
        try (Connection con = ds.getConnection()) {
        	ProductoRepository repo = new ProductoRepository(con);
            return repo.findResumen();
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
    
    
    //pruebas
    @GetMapping
    public List<ProductoResumen> index(
        @RequestParam(required = false) String categoria,
        @RequestParam(required = false) Float precioMin,
        @RequestParam(required = false) Float precioMax,
        @RequestParam(required = false, defaultValue = "nombre-asc") String orden
    ) {
        try (Connection con = ds.getConnection()) {
            ProductoRepository repo = new ProductoRepository(con);
            Stream<ProductoResumen> stream = repo.findResumen().stream();

            // Filtro categoría
            if (categoria != null && !categoria.isBlank()) {
                stream = stream.filter(p ->
                    p.nombre().toLowerCase().contains(categoria.toLowerCase())
                );
            }
            if (precioMin != null) {
                stream = stream.filter(p -> p.precio() >= precioMin);
            }
            if (precioMax != null) {
                stream = stream.filter(p -> p.precio() <= precioMax);
            }

            Comparator<ProductoResumen> comparator = switch (orden) {
            case "nombre-desc" -> Comparator.comparing(ProductoResumen::nombre).reversed();
            case "precio-asc"  -> Comparator.comparing(ProductoResumen::precio);
            case "precio-desc" -> Comparator.comparing(ProductoResumen::precio).reversed();
            default            -> Comparator.comparing(ProductoResumen::nombre);
            };

            return stream.sorted(comparator).collect(Collectors.toList());

        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
    
}
