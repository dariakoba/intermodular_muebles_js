package muebles.exception;

@SuppressWarnings("serial")
public class NotFoundException extends RuntimeException{
    public NotFoundException() {
        super("Recurso no encontrado");
    }
    
    public NotFoundException(String message) {
        super(message);
    }
    
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public NotFoundException(Throwable cause) {
        super(cause);
    }
    
    public static void ifNull(Object o, String message) {
    	if (o == null) throw new NotFoundException(message);
    }
}
