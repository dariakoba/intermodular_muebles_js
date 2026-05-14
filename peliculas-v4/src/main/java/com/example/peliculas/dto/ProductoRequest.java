package com.example.peliculas.dto;

import java.time.LocalDateTime;

public record ProductoRequest (
	String nombre,
    String color,
    float precio,
    int stock,
    String descripcion,
    Integer categoriaId,
    LocalDateTime deletedAt
) {}
