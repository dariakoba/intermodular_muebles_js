package com.example.peliculas.dto.auth;

public record RegisterRequest(
	String name, 
	String email, 
	String password
) {}
