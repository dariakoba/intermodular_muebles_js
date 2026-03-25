package com.example.peliculas.dto;

import java.time.LocalDate;

public record VotoDTO(
	int id,
	String user,
	int puntuacion,
	String critica,
	LocalDate fecha) {
}
