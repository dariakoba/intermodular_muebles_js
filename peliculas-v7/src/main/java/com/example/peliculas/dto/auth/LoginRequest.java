package com.example.peliculas.dto.auth;

public record LoginRequest(
	String email,
	String password
) {}
