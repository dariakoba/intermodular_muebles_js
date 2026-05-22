package com.dna.muebles.dto.auth;

public record RegisterRequest(
	String nombre, 
	String apellidos, 
	String email, 
	String passwordHash,
	String telefono
) {}
