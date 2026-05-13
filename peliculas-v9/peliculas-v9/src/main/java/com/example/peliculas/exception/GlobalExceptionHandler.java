package com.example.peliculas.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

	// BAD REQUEST -> 400
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<?> handleBadRequest(BadRequestException e) {
		logError(e);
	    return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
	}
	
	// VALIDACIÓN -> 400
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException e) {

		logError(e);
		
		Map<String, String> errors = new LinkedHashMap<>();

		e.getBindingResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});

		return ResponseEntity.badRequest().body(Map.of(
			"message", "Error de validación", "errors", errors));
	}
	
	// NOT FOUND -> 404
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Map<String, String>> handleNotFound(NotFoundException e) {
		logError(e);
		return ResponseEntity.status(404).body(Map.of("message", e.getMessage()));
	}
	
	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<Void> handleNoResource() {
		return ResponseEntity.notFound().build();
	}

	// DUPLICATE KEY -> 409
	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<Map<String, String>> handleDuplicate(DuplicateKeyException e) {
		logError(e);
		return ResponseEntity.status(409).body(Map.of("message", "El recurso ya existe"));
	}

	// DATA ACCESS -> 500
	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<Map<String, String>> handleData(DataAccessException e) {
		logError(e);
		return ResponseEntity.status(500).body(Map.of("message", "Error de acceso a datos"));
	}
	
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<Map<String, String>> handleResponseStatus(ResponseStatusException e) {

		logError(e);

		return ResponseEntity.status(e.getStatusCode())
				.body(Map.of("message", e.getReason() != null ? e.getReason() : "Error"));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handleGeneric(Exception e) {
		logError(e);
		return ResponseEntity.status(500).body(Map.of("message", "Error interno del servidor"));
	}

	private void logError(Exception e) {
		System.err.println(e.getMessage());
		e.printStackTrace();
	}
}