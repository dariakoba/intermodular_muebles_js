package app.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.entity.Articulo;
import app.entity.Ventas;
import app.exception.DataAccesException;

public class VentaRepository {

private Connection con;
	
	public VentaRepository(Connection con) {
		this.con=con;
	}

	
	public Ventas insert(Ventas venta) {

		String sql = "INSERT INTO Ventas (articulo_id, cantidad, total)" + "VALUES (?,?,?)";
		try (PreparedStatement stmt = this.con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);) {

			stmt.setInt(1, venta.getId());
			stmt.setInt(2, venta.getCantidad());
			stmt.setDouble(3, venta.getTotal());

			stmt.executeUpdate();
			ResultSet rs= stmt.getGeneratedKeys();
			if (rs.next()) {
				int id = rs.getInt(1);
				venta.setId(null);
				
				return venta;
			}
		
			throw new RuntimeException("No tiene campo autoincremental");

		} 
		catch (SQLException e) {
			throw new DataAccesException ("Error guardando las ventas", e);
		}

	}
	
	
	
}
