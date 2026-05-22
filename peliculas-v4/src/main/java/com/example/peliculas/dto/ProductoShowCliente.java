package com.example.peliculas.dto;

import java.util.List;

public record ProductoShowCliente(
	String nombre,
	String color,
	String descripcion,
	float precio,
	List<ImagenResponse> imagenes

	
) {}
