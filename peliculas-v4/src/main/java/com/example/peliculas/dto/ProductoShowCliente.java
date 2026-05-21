package com.example.peliculas.dto;

public record ProductoShowCliente(
	Integer idProducto,
	String nombre,
	String descripcion,
	float precio,
	String categoria,
	String imagen
	
) {}
