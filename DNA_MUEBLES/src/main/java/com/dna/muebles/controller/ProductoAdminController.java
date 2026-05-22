package com.dna.muebles.controller;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.web.bind.annotation.*;

import com.dna.muebles.dto.ImagenResponse;
import com.dna.muebles.dto.ProductoDetalle;
import com.dna.muebles.dto.ProductoRequest;
import com.dna.muebles.dto.ProductoResumen;
import com.dna.muebles.entity.Producto;
import com.dna.muebles.exception.DataAccessException;
import com.dna.muebles.repository.ProductoImagenRepository;
import com.dna.muebles.repository.ProductoRepository;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/admin/productos")
public class ProductoAdminController {

    private final DataSource ds;

    public ProductoAdminController(DataSource ds) {
        this.ds = ds;
    }
    
	// INDEX 
	@GetMapping
	public List<ProductoResumen> index() {

		try (Connection con = ds.getConnection();) {
			ProductoRepository repo = new ProductoRepository(con);
			return repo.findAllResumenNoImagen();

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
  
    
	// SHOW
    @GetMapping("/{id}")
    public ProductoDetalle show(@PathVariable int id) {

        try (Connection con = ds.getConnection()) {
            ProductoRepository repo = new ProductoRepository(con);
            ProductoImagenRepository imgRepo = new ProductoImagenRepository(con);

            ProductoDetalle detalle = repo.findDetalle(id);
            List<ImagenResponse> imagenes = imgRepo.findByProductoId(id);

            return new ProductoDetalle(
                detalle.idProducto(),
                detalle.nombre(),
                detalle.color(),
                detalle.precio(),
                detalle.stock(),
                detalle.descripcion(),
                detalle.categoriaId(),
                detalle.categoria(),
                detalle.estado(),
                imagenes  // ← aquí las inyectas
            );

        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
    
//	@GetMapping("/{id}")
//	public ProductoDetalle show(@PathVariable int id) {
//
//		try (Connection con = ds.getConnection();) {
//			ProductoRepository repo = new ProductoRepository(con);
//			ProductoImagenRepository imgRepo = new ProductoImagenRepository(con);
//
//			Producto p = repo.findOrThrow(id);
//			List<ImagenResponse> imagenes = imgRepo.findByProductoId(id);
//			
//			return new ProductoDetalle(
//				p.getIdProducto(), 
//				p.getNombre(), 
//				p.getColor(),
//				p.getPrecio(),
//				p.getStock(),
//				p.getDescripcion(),
//				p.getCategoriaId(),
//				p.getDeletedAt(),
//				imagenes
//			);
//
//		} catch (SQLException e) {
//			throw new DataAccessException(e);
//		}
//	}


	//crear producto 
    @PostMapping
	@ResponseStatus(HttpStatus.CREATED)
    public Producto store(@RequestBody ProductoRequest req) {
        try (Connection con = ds.getConnection()) {
			ProductoRepository repo = new ProductoRepository(con);
			Producto p = new Producto(null, req.nombre(), req.color(), req.precio(), req.stock()
					, req.descripcion(), req.categoriaId(), req.deletedAt());
			return repo.insert(p);

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