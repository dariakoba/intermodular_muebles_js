package com.example.peliculas.dto;

public record UserResponse(
	Integer id,
	String name,
	String email,
	String role
) {}
