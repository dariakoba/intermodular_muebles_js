package com.example.peliculas.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.*;

import com.example.peliculas.dto.ImagenResponse;
import com.example.peliculas.dto.ProductoDetalle;
import com.example.peliculas.dto.ProductoResumenImagen;
import com.example.peliculas.dto.ProductoShowCliente;
import com.example.peliculas.entity.Producto;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.repository.ProductoImagenRepository;
import com.example.peliculas.repository.ProductoRepository;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
	private final DataSource ds;
    public ProductoController(DataSource ds) {
        this.ds = ds;
    }
    
	// SHOW
	@GetMapping("/{id}")
	public ProductoShowCliente show(@PathVariable int id) {

		try (Connection con = ds.getConnection();) {
			ProductoRepository repo = new ProductoRepository(con);
			ProductoImagenRepository imgRepo = new ProductoImagenRepository(con);

			Producto p = repo.findOrThrow(id);
			List<ImagenResponse> imagenes = imgRepo.findByProductoId(id);
			
			return new ProductoShowCliente(
				p.getNombre(), 
				p.getColor(),
				p.getDescripcion(),
				p.getPrecio(),
				imagenes
			);

		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
   
    
    //pruebas
	@GetMapping
	public List<ProductoResumenImagen> index(
	    @RequestParam(required = false) List<String> categoria,
	    @RequestParam(required = false) List<String> color,
	    @RequestParam(required = false) Float precioMin,
	    @RequestParam(required = false) Float precioMax,
	    @RequestParam(required = false, defaultValue = "nombre-asc") String orden
	) {
	    try (Connection con = ds.getConnection()) {
	        ProductoRepository repo = new ProductoRepository(con);
	        Stream<ProductoResumenImagen> stream = repo.findAllResumen().stream();

	        // Filtro categorías — si hay varias, muestra productos de cualquiera
	        if (categoria != null && !categoria.isEmpty()) {
	            stream = stream.filter(p ->
	                p.categoria() != null &&
	                categoria.stream().anyMatch(c -> c.equalsIgnoreCase(p.categoria()))
	            );
	        }

	        // Filtro colores — igual
	        if (color != null && !color.isEmpty()) {
	            stream = stream.filter(p ->
	                p.color() != null &&
	                color.stream().anyMatch(c -> c.equalsIgnoreCase(p.color()))
	            );
	        }

	        if (precioMin != null) stream = stream.filter(p -> p.precio() >= precioMin);
	        if (precioMax != null) stream = stream.filter(p -> p.precio() <= precioMax);

	        Comparator<ProductoResumenImagen> comparator = switch (orden) {
	            case "nombre-desc" -> Comparator.comparing(ProductoResumenImagen::nombre).reversed();
	            case "precio-asc"  -> Comparator.comparing(ProductoResumenImagen::precio);
	            case "precio-desc" -> Comparator.comparing(ProductoResumenImagen::precio).reversed();
	            default            -> Comparator.comparing(ProductoResumenImagen::nombre);
	        };

	        return stream.sorted(comparator).collect(Collectors.toList());

	    } catch (SQLException e) {
	        throw new DataAccessException(e);
	    }
	}
}
