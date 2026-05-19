package app.controller;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import app.database.DB;
import app.database.Tx;
import app.entity.Articulo;
import app.entity.Ventas;
import app.exception.BusinessException;
import app.exception.DataAccesException;
import app.repository.ArticuloRepository;
import app.repository.VentaRepository;
import app.view.ArticuloView;


public class ArticuloController {
	
	public void index() {
		
		try (Connection con= DB.getConnection()){
			var repo = new ArticuloRepository(con);
			ArticuloView.index(repo.findAll());
			
			
		}catch (SQLException e) {
			throw new DataAccesException(e);
			
		}
		
	}
	
	public void vender() {
		
		Tx.run(con -> {
			
			var articuloRepo = new ArticuloRepository(con);
			var ventaRepo = new VentaRepository(con);
			List<Articulo> articulos = articuloRepo.findAll();
			ArticuloView.index(articulos);

			int id = Integer.parseInt(ArticuloView.solicitarEntrada("Dame el id del articulo: "));
			int cantidad = Integer.parseInt(ArticuloView.solicitarEntrada("Dame la cantidad: "));
			Articulo articulo = articuloRepo.find(id);

			if (articuloRepo.decrementStock(articulo, cantidad) == 0) {
				throw new BusinessException("no hay suficiente stock");
			}

			Ventas venta = new Ventas(null, id, cantidad, cantidad * articulo.getPrecio());
			ventaRepo.insert(venta);
			
			return venta;
			
		});


			ArticuloView.mostrarMensaje("Venta registrada");

	}

	/* SIN TX
	 * public void vender() {

		try (Connection con = DB.getConnection()) {

			try {
				con.setAutoCommit(false);
				var articuloRepo = new ArticuloRepository(con);
				var ventaRepo = new VentaRepository(con);
				List<Articulo> articulos = articuloRepo.findAll();
				ArticuloView.index(articulos);

				int id = Integer.parseInt(ArticuloView.solicitarEntrada("Dame el id del articulo: "));
				int cantidad = Integer.parseInt(ArticuloView.solicitarEntrada("Dame la cantidad: "));
				Articulo articulo = articuloRepo.find(id);

				if (articuloRepo.decrementStock(articulo, cantidad) == 0) {
					throw new BusinessException("no hay suficiente stock");
				}

				Ventas venta = new Ventas(null, id, cantidad, cantidad * articulo.getPrecio());
				ventaRepo.insert(venta);
				con.commit();

			} catch (Exception e) {
				con.rollback();
				throw e;
			}

			ArticuloView.mostrarMensaje("Venta registrada");

		}*/
	 
}
