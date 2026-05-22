package com.example.peliculas.dto;

public record ProductoResumenImagen(
	Integer idProducto,
	String nombre,
	float precio,
	int stock,
	String categoria,
    String color,
	String imagen
	
) {}
