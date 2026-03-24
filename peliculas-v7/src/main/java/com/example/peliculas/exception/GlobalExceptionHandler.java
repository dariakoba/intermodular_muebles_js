package com.example.peliculas.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

	// VALIDACIÓN -> 400
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {

		Map<String, String> errors = new LinkedHashMap<>();

		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});

		Map<String, Object> response = new HashMap<>();
		response.put("errors", errors);

		return ResponseEntity.badRequest().body(response);
	}
	
	// DUPLICATE KEY -> 409
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(DuplicateKeyException e) {

        Map<String, Object> response = new HashMap<>();
        response.put("message", "El recurso ya existe");

        return ResponseEntity.status(409).body(response);
    }

    // DATA ACCESS -> 500
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleData(DataAccessException e) {

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Error de acceso a datos");

        return ResponseEntity.status(500).body(response);
    }
}