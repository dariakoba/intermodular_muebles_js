package app.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

import app.exception.DataAccesException;

//ESTO LO PREGUNTARA EN PAPEL
public class Tx {

	public static <T> T run(Function<Connection, T> fn) {
		try (Connection con = DB.getConnection()) {
			try {
				con.setAutoCommit(false);
				T result = fn.apply(con);
				con.commit();
				return result;
			} catch (Exception e) {
				con.rollback();
				throw e;
			}

		} catch (SQLException e) {
			throw new DataAccesException(e);
		}
	}
}
