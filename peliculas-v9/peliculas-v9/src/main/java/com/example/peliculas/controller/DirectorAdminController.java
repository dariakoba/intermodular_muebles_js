package com.example.peliculas.controller;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.peliculas.dto.DirectorDetalleResponse;
import com.example.peliculas.dto.DirectorRequest;
import com.example.peliculas.dto.DirectorResumenResponse;
import com.example.peliculas.dto.ImagenResponse;
import com.example.peliculas.entity.Director;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.exception.NotFoundException;
import com.example.peliculas.helper.StorageHelper;
import com.example.peliculas.repository.DirectorImagenRepository;
import com.example.peliculas.repository.DirectorRepository;

@RestController
@RequestMapping("/api/admin/directores")
public class DirectorAdminController extends BaseController {
	
	private final StorageHelper storage;

	public DirectorAdminController(DataSource ds, StorageHelper storage) {
		super(ds);
		this.storage = storage;
	}

	// INDEX
	@GetMapping
	public List<DirectorResumenResponse> index() {

		try (Connection con = ds.getConnection();) {
			DirectorRepository repo = new DirectorRepository(con);
			return repo.findAllResumen();

		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	// SHOW
	@GetMapping("/{id}")
	public DirectorDetalleResponse show(@PathVariable int id) {

		try (Connection con = ds.getConnection();) {
			DirectorRepository repo = new DirectorRepository(con);
			DirectorImagenRepository imgRepo = new DirectorImagenRepository(con);

			Director d = repo.findOrThrow(id);
			List<ImagenResponse> imagenes = imgRepo.findByDirectorId(id);
			
			return new DirectorDetalleResponse(
				d.getId(), 
				d.getNombre(), 
				d.getPais(), 
				imagenes
			);

		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	// CREATE
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Director store(@RequestBody DirectorRequest req) {

		try (Connection con = ds.getConnection()) {

			DirectorRepository repo = new DirectorRepository(con);
			Director d = new Director(null, req.nombre(), req.pais());
			return repo.insert(d);

		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	// UPDATE
	@PutMapping("/{id}")
	public Director update(@PathVariable int id, @RequestBody DirectorRequest req) {

		try (Connection con = ds.getConnection()) {

			DirectorRepository repo = new DirectorRepository(con);
			Director d = new Director(id, req.nombre(), req.pais());

			if (repo.update(d) == 0) {
				throw new NotFoundException();
			}
			
			return d;

		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}

	// DELETE
	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {

		try (Connection con = ds.getConnection()) {

			DirectorRepository repo = new DirectorRepository(con);
			DirectorImagenRepository imgRepo = new DirectorImagenRepository(con);

			// 1. obtener imágenes
			var imagenes = imgRepo.findByDirectorId(id);

			// 2. borrar director
			if (repo.delete(id) == 0) {
				throw new NotFoundException();
			}

			// 3. borrar archivos físicos
			for (var imagen : imagenes) {
				storage.deleteByUrl(imagen.url());
			}

		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
}
