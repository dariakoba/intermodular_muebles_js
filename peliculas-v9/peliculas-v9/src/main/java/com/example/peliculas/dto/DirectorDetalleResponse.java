package com.example.peliculas.dto;

import java.util.List;

public record DirectorDetalleResponse(
	int id, 
	String nombre, 
	String pais, 
	List<ImagenResponse> imagenes
) {}