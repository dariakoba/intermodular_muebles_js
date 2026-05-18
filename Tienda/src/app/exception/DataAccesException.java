package app.exception;

@SuppressWarnings("serial")
public class DataAccesException extends RuntimeException{

	public DataAccesException() {
		super("Error de acceso a datos");
	}
	
	public DataAccesException(String message) {
		super(message);
	}
	
	public DataAccesException(Throwable cause) {
		super(cause);
	}
	
	public DataAccesException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
