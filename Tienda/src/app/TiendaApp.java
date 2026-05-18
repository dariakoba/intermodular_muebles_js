package app;

import app.controller.ArticuloController;
import app.controller.MainController;

public class TiendaApp {

	public static void main(String[] args) {

		ArticuloController ac = new ArticuloController();
		MainController mc = new MainController(ac);
		mc.run();
	}

}
