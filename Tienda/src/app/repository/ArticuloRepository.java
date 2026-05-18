package app.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.entity.Articulo;
import app.exception.DataAccesException;

public class ArticuloRepository {

	
	private Connection con;
	
	public ArticuloRepository(Connection con) {
		this.con=con;
	}
	
	public List<Articulo> findAll() {

		String sql = "SELECT * FROM articulos";
		try (PreparedStatement stmt = this.con.prepareStatement(sql);) {

			List<Articulo> articulos = new ArrayList<>();

			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				articulos.add(map(rs));

			}
			
			return articulos;

		}catch (SQLException e) {
			throw new DataAccesException ("Error buscando todos los articulos", e);
		}

	}
	
	public Articulo find(int id) {

		String sql = "SELECT * FROM articulos WHERE id=?";
		try (PreparedStatement stmt = this.con.prepareStatement(sql);) {

			stmt.setInt(1, id);
			
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				return map(rs);

			}
			
			return null;

		}catch (SQLException e) {
			throw new DataAccesException ("Error buscando el articulo" + id, e);
		}

	}
	
	public int decrementStock(Articulo articulo, int cantidad) {
		
		String sql = "UPDATE articulos SET stock = stock - ?"
				+ " WHERE id = ? "
				+ "AND stock  >= ?";
		try(PreparedStatement stmt = this.con.prepareStatement(sql)){
			stmt.setInt(1, cantidad);
			stmt.setInt(2, articulo.getId());
			stmt.setInt(3, cantidad);
			
			return stmt.executeUpdate();

			
		}catch (SQLException e) {
			throw new DataAccesException("error actualizando stock" + articulo);
		}
	}
	
	private Articulo map(ResultSet rs) throws SQLException{
		return new Articulo (
				rs.getInt("id"),
				rs.getString("nombre"),
				rs.getInt("stock"),
				rs.getDouble("precio")
		);
	}
	
	//INSERT INTO articulos (nombre, stock, precio) VALUES = (?,?,?)
	//UPDATE articulos SET nombre=?, stock=?, precio=? where id=?
	//DELETE FROM articulos WHERE id=?
	
	
	
	
}
