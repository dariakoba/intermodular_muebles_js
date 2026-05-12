package com.example.peliculas.controller;

import java.io.IOException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.peliculas.entity.DirectorImagen;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.helper.StorageHelper;
import com.example.peliculas.repository.DirectorImagenRepository;
import com.example.peliculas.repository.DirectorRepository;
import com.example.peliculas.validation.ImageValidator;

@RestController
@RequestMapping("/api/admin/directores/{directorId}/imagenes")
public class DirectorImagenController extends BaseController {

	private final StorageHelper storage;

	public DirectorImagenController(DataSource ds, StorageHelper storage) {
		super(ds);
		this.storage = storage;
	}

	// CREATE
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public DirectorImagen store(@PathVariable int directorId, @RequestParam("file") MultipartFile file) {

		try (Connection con = ds.getConnection()) {

			// validar existencia
			new DirectorRepository(con).findOrThrow(directorId);
			
			ImageValidator.validate(file);

			String url = storage.save(file, "directores");

			DirectorImagenRepository repo = new DirectorImagenRepository(con);

			DirectorImagen img = new DirectorImagen(null, directorId, url);

			return repo.insert(img);

		} catch (SQLException e) {
			throw new DataAccessException(e);
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// DELETE
	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {

		try (Connection con = ds.getConnection()) {

			DirectorImagenRepository repo = new DirectorImagenRepository(con);

			// opcional: obtener url antes de borrar
			DirectorImagen img = repo.find(id);

			repo.delete(id);

			if (img != null && img.getUrl() != null) {
				storage.deleteByUrl(img.getUrl());
			}

		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
}