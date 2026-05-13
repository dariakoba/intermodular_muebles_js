package com.example.peliculas.validation;


import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import com.example.peliculas.exception.BadRequestException;

public class ImageValidator {

	private static final Tika tika = new Tika();

	private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/webp");

	private static final long MAX_SIZE = 2_000_000; // 2MB

	public static void validate(MultipartFile file) {

		if (file == null || file.isEmpty()) {
			throw new BadRequestException("Archivo vacío");
		}

		if (file.getSize() > MAX_SIZE) {
			throw new BadRequestException("Máximo 2MB");
		}

		String mimeType;

		try (InputStream is = file.getInputStream()) {
			mimeType = tika.detect(is);
		} catch (IOException e) {
			throw new BadRequestException("No se pudo procesar el archivo");
		}

		if (!ALLOWED_TYPES.contains(mimeType)) {
			throw new BadRequestException("Formato no permitido (jpg, png, webp)");
		}
	}
}

