package app.controller;

import app.view.ArticuloView;

public class MainController {

	private ArticuloController controller;
	private ErrorController errorController;

	
	public MainController (ArticuloController controller, ErrorController errorController) {
		this.controller = controller;
		this.errorController = errorController;

	}
	
	public void run() {
		String opcion= "";
		do {
			try {
				opcion = ArticuloView.menu();
				switch(opcion) {
				case "1" -> controller.index();
				case "2" -> controller.vender();

				default -> System.out.println("no existe la opcion");
				
				}
				
			} catch(Exception e) {
				this.errorController.handle(e);
			}
		
			
		}while(!opcion.equals("0"));
		
		
	
	
}
}
