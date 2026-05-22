package com.dna.muebles.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.dna.muebles.entity.Categoria;

public class CategoriaMapper implements RowMapper<Categoria> {
	@Override
    public Categoria mapRow(ResultSet rs) throws SQLException {
        return new Categoria(
                rs.getInt("id_categoria"),
                rs.getString("nombre"),
				rs.getObject("deleted_at", java.time.LocalDateTime.class)
        );
    }
}
