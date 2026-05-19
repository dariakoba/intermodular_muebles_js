package app.controller;

import app.exception.DataAccesException;
import app.view.ArticuloView;

public class ErrorController {

	public void handle(Exception e) {
		logError(e);
		String mensaje;
		if(e instanceof DataAccesException ) {
			mensaje = "Error de acceso a datos\n" + e.getMessage();
			ArticuloView.mostrarMensaje(mensaje);
			return;
		}
		
		ArticuloView.mostrarMensaje("Error interno");
		System.out.println(e.getMessage());
		e.printStackTrace();
	}
	
	private void logError(Exception e) {
		System.out.println(e.getMessage());
		e.printStackTrace();
	}
	
}
