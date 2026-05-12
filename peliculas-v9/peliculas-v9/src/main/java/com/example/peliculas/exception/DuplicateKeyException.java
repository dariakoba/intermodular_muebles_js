package com.example.peliculas.exception;

@SuppressWarnings("serial")
public class DuplicateKeyException extends DataAccessException {
	
    public DuplicateKeyException(String message) {
        super(message);
    }
    
    public DuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DuplicateKeyException(Throwable cause) {
        super(cause);
    }
}
