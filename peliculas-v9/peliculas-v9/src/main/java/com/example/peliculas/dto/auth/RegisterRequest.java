package com.example.peliculas.dto.auth;

import jakarta.validation.constraints.*;

public record RegisterRequest(

		@NotBlank(message = "El nombre es obligatorio")
		@Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
		String name,

		@NotBlank(message = "El email es obligatorio") 
		@Email(message = "El email no es válido") 
		@Size(max = 100, message = "El email no puede superar los 100 caracteres") 
		String email,

		@NotBlank(message = "La contraseña es obligatoria") 
		@Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres") 
		String password

) {}
