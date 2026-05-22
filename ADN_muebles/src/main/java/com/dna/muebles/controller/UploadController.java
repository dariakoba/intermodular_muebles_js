package com.dna.muebles.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.dna.muebles.exception.BadRequestException;
import com.dna.muebles.validation.ImageValidator;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

    private static final String BASE_DIR = "uploads";

    @PostMapping("/{folder}")
    public ResponseEntity<?> upload(
            @PathVariable String folder,
            @RequestParam("file") MultipartFile file
    ) {

        // SOLO carpetas permitidas
        if (!List.of("usuarios", "productos", "resenyas").contains(folder)) {
            throw new BadRequestException("Carpeta no válida");
        }

        // validar imagen
        ImageValidator.validate(file);

        try {

            // nombre original
            String original = file.getOriginalFilename();

            // extensión
            String extension =
                original.substring(original.lastIndexOf("."));

            // nombre único
            String filename =
                UUID.randomUUID() + extension;

            // ruta física
            Path path = Paths.get(
                BASE_DIR,
                folder,
                filename
            );

            // crear carpeta si no existe
            Files.createDirectories(path.getParent());

            // guardar archivo
            Files.write(path, file.getBytes());

            // URL pública
            String url =
                "/uploads/" + folder + "/" + filename;

            return ResponseEntity.ok(
                Map.of("url", url)
            );

        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity
                .internalServerError()
                .body("Error al subir archivo");
        }
    }
}