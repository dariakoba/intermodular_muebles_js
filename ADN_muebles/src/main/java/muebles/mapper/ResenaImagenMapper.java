package muebles.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import muebles.entity.ResenaImagen;

public class ResenaImagenMapper implements RowMapper<ResenaImagen> {

    @Override
    public ResenaImagen mapRow(ResultSet rs) throws SQLException {

        return new ResenaImagen(
            rs.getInt("id"),
            rs.getInt("resena_id"),
            rs.getString("url")
        );
    }
}