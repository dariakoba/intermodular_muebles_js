package com.example.peliculas.dto;

public record ProductoResumen(
	Integer idProducto,
	String nombre,
	String descripcion,
	float precio
	
) {}
