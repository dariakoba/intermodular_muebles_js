package com.example.peliculas.dto;



public record UserRequest(
		String passwordHash,         
	    String rol,            // rol VARCHAR(20)
	    String telefono,         // telefono VARCHAR(20)
	    String estado,           // estado VARCHAR(20)
	    String nombre,            // nombre VARCHAR(50)
	    String apellidos,         // apellidos VARCHAR(50)
	    String direccion,        // direccion VARCHAR(100)
	    String email
			) {

}
