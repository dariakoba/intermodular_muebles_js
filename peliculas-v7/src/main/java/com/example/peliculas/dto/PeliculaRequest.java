package com.example.peliculas.dto;

import jakarta.validation.constraints.*;

public record PeliculaRequest(

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 255, message = "El título no puede superar los 255 caracteres")
    String titulo,

    @Min(value = 1900, message = "El año mínimo es 1900")
    @Max(value = 2100, message = "El año no puede ser superior a 2100")
    int anyo,

    @Positive(message = "La duración debe ser mayor que 0")
    int duracion,

    @NotBlank(message = "La sinopsis es obligatoria")
    @Size(max = 2000, message = "La sinopsis no puede superar los 2000 caracteres")
    String sinopsis,

    @Positive(message = "Debe seleccionar un director")
    int directorId

) {}

 