package com.example.peliculas.dto;

import java.time.LocalDate;

public record UserVote(
	int puntuacion, 
	String critica, 
	LocalDate fecha
) {}
