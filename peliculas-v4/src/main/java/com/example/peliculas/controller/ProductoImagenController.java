package com.example.peliculas.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.peliculas.dto.ProductoCatNomDetalle;
import com.example.peliculas.entity.Producto;
import com.example.peliculas.entity.ProductoImagen;
import com.example.peliculas.helper.StorageHelper;

import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.repository.ProductoImagenRepository;
import com.example.peliculas.repository.ProductoRepository;
import com.example.peliculas.validation.ImageValidator;



@RestController
@RequestMapping("/api/admin/productos/{productoId}/imagenes")
public class ProductoImagenController {
	 private final DataSource ds;
	 private final StorageHelper storage;

	  public ProductoImagenController(DataSource ds, StorageHelper storage) {
			this.ds = ds;
			this.storage = storage;

	    }
	    
	    
	  
		// CREATE
		@PostMapping
		@ResponseStatus(HttpStatus.CREATED)
		public ProductoImagen store(@PathVariable int productoId, @RequestParam("file") MultipartFile file) {

			try (Connection con = ds.getConnection()) {

				// validar existencia
				new ProductoRepository(con).findOrThrow(productoId);
				
				ImageValidator.validate(file);

				String url = storage.save(file, "productos");

				ProductoImagenRepository repo = new ProductoImagenRepository(con);

				ProductoImagen img = new ProductoImagen(null, productoId, url);

				return repo.insert(img);

			} catch (SQLException e) {
				throw new DataAccessException(e);
			} catch (IOException e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

	 
		//delete
		@DeleteMapping("/{id}")
		public void delete(@PathVariable int id) {

			try (Connection con = ds.getConnection()) {

				ProductoImagenRepository repo = new ProductoImagenRepository(con);

				// opcional: obtener url antes de borrar(porsiacaso)
				ProductoImagen img = repo.find(id);

				repo.delete(id);

				if (img != null && img.getUrl() != null) {
					storage.deleteByUrl(img.getUrl());
				}

			} catch (SQLException e) {
				throw new DataAccessException(e);
			}
		}
}
