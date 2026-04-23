package com.example.peliculas.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class StorageHelper {

	private static final String BASE_DIR = "uploads";

	public String save(MultipartFile file, String folder) throws IOException {

		String original = file.getOriginalFilename();

		// evitar NPE + extensión opcional
		String extension = "";
		if (original != null && original.contains(".")) {
			extension = original.substring(original.lastIndexOf("."));
		}

		String filename = UUID.randomUUID() + extension;

		Path path = Paths.get(BASE_DIR, folder, filename);

		Files.createDirectories(path.getParent());
		Files.write(path, file.getBytes());

		return "/uploads/" + folder + "/" + filename;
	}

	public void deleteByUrl(String url) {

		Path path = resolvePath(url);

		if (path == null)
			return;

		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			// log opcional
			System.err.println("Error al eliminar archivo: " + path + " -> " + e.getMessage());
		}
	}

	public boolean exists(String url) {

		Path path = resolvePath(url);

		return path != null && Files.exists(path);
	}
	
	private Path resolvePath(String url) {

		if (url == null || !url.startsWith("/uploads/"))
			return null;

		String relative = url.replace("/uploads/", "");
//
		Path base = Paths.get(BASE_DIR).toAbsolutePath().normalize();
		Path path = base.resolve(relative).normalize();

		// protección path traversal
		if (!path.startsWith(base))
			return null;

		return path;
	}
}
