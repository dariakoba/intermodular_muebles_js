package com.example.peliculas.dto;

import java.time.LocalDate;

public record UserResponse(
		int id,                  // id SERIAL -> int
	    String rol,            // rol VARCHAR(20)
	    String telefono,         // telefono VARCHAR(20)
	    String estado,           // estado VARCHAR(20)
	    String nombre,            // nombre VARCHAR(50)
	    String apellidos,         // apellidos VARCHAR(50)
	    String direccion,        // direccion VARCHAR(100)
	    String email,
	    int puntos,
	    int nivelAcceso,
	    float salario,
	    LocalDate fechaAlta

	  
) {}
