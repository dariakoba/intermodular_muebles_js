package com.example.peliculas.dto;

public record ProductoResumen(
	int idProducto,
	String nombre,
	String descripcion,
	float precio
) {}
