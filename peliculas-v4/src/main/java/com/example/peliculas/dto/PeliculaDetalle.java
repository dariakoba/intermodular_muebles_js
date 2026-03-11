package com.example.peliculas.dto;

public record PeliculaDetalle(
	String titulo,
	int anyo,
	int duracion,
	String sinopsis,
	String director
) {}
