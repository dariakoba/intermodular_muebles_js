package muebles.dto;

public record ProductoResumen(
	Integer idProducto,
	String nombre,
	float precio,
	int stock,
	String estado,
	String categoria
	//String imagen
	
) {}
