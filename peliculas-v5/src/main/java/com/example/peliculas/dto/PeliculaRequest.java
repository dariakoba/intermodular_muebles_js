package com.example.peliculas.dto;

public record PeliculaRequest(
	String titulo,
	int anyo,
	int duracion,
	String sinopsis,
	int directorId
) {}
 