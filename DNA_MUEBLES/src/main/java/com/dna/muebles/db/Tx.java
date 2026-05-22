package com.dna.muebles.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;
import javax.sql.DataSource;

import com.dna.muebles.exception.DataAccessException;

public class Tx {
	private Tx() {
		// Evita instanciación
	}

	public static <T> T run(DataSource ds, Function<Connection, T> fn) {
		
		try (Connection con = ds.getConnection()) {
			
			con.setAutoCommit(false);
			
			try {
				T result =  fn.apply(con);
				con.commit();
				return result;
				
			} catch (Exception e) {
				con.rollback();
				throw e;
			}
			
		} catch (SQLException e) {
			throw new DataAccessException(e);
		}
	}
}