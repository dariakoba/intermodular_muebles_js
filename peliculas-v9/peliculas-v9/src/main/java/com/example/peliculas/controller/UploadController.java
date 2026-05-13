package com.example.peliculas.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.peliculas.exception.BadRequestException;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

	private static final String BASE_DIR = "uploads";

	@PostMapping("/{folder}")
	public ResponseEntity<?> upload(@PathVariable String folder, @RequestParam("file") MultipartFile file) {

		// validar carpeta (lista blanca)
		if (!List.of("peliculas", "usuarios", "directores").contains(folder)) {
			    throw new BadRequestException("Carpeta no válida");
		}

		// validar archivo
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body("Archivo vacío");
		}

		if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
			return ResponseEntity.badRequest().body("Solo imágenes");
		}

		try {
			// extensión
			String original = file.getOriginalFilename();
			String extension = original.substring(original.lastIndexOf("."));

			// nombre único
			String filename = UUID.randomUUID() + extension;

			// ruta
			Path path = Paths.get(BASE_DIR, folder, filename);

			// crear carpetas si no existen
			Files.createDirectories(path.getParent());

			// guardar archivo
			Files.write(path, file.getBytes());

			// url pública
			String url = "/uploads/" + folder + "/" + filename;

			return ResponseEntity.ok(Map.of("url", url));

		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error al subir archivo");
		}
	}
}

/*
@DeleteMapping
public ResponseEntity<?> delete(@RequestParam String url) {

	try {
		// 🔒 validar que pertenece a /uploads/
		if (url == null || !url.startsWith("/uploads/")) {
			return ResponseEntity.badRequest().body("Ruta no válida");
		}

		// convertir URL → path físico
		String relativePath = url.replace("/uploads/", "");
		Path path = Paths.get(BASE_DIR, relativePath);

		// 🔒 evitar path traversal
		if (!path.normalize().startsWith(BASE_DIR)) {
			return ResponseEntity.badRequest().body("Ruta no permitida");
		}

		// borrar si existe
		boolean deleted = Files.deleteIfExists(path);

		if (!deleted) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().build();

	} catch (Exception e) {
		return ResponseEntity.status(500).body("Error al eliminar archivo");
	}
}
*/
