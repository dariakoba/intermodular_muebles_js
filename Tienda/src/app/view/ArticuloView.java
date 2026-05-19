package app.view;

import java.util.List;

import javax.swing.JOptionPane;

import app.entity.Articulo;

public class ArticuloView {
	
	public static String menu() {
		String menu = """
				1- Listar articulos
				2- Comprar
				
				0- Salir
				""";
		return JOptionPane.showInputDialog(menu);
	}
	public static void index(List<Articulo> articulos) {
		
		for(Articulo a : articulos) {
			System.out.println(a);
		}
	}
	
	public static void mostrarMensaje(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje);
	}

	public static String solicitarEntrada(String mensaje) {
		return JOptionPane.showInputDialog(mensaje);
		
	}
}
