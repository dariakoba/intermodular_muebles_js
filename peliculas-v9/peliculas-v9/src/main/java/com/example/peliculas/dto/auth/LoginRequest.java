package com.example.peliculas.dto.auth;

import jakarta.validation.constraints.*;

public record LoginRequest(

	@NotBlank(message = "El email es obligatorio") 
	String email,

	@NotBlank(message = "La contraseña es obligatoria")
	String password

) {}