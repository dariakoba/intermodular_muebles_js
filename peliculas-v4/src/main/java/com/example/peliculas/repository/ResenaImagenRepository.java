package com.example.peliculas.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.example.peliculas.db.DB;
import com.example.peliculas.dto.ImagenResponse;
import com.example.peliculas.entity.ResenaImagen;
import com.example.peliculas.exception.DataAccessException;
import com.example.peliculas.mapper.ResenaImagenMapper;
import com.example.peliculas.mapper.RowMapper;

public class ResenaImagenRepository extends BaseRepository<ResenaImagen> {

    public ResenaImagenRepository(Connection con) {
        super(con, new ResenaImagenMapper());
    }

    public ResenaImagenRepository(Connection con, RowMapper<ResenaImagen> mapper) {
        super(con, mapper);
    }

    @Override
    public String getTable() {
        return "resena_imagenes";
    }

    @Override
    public String[] getColumnNames() {
        return new String[] { "id", "resena_id", "url" };
    }

    @Override
    public Integer getPrimaryKey(ResenaImagen ri) {
        return ri.getId();
    }

    @Override
    public void setPrimaryKey(ResenaImagen ri, int id) {
        ri.setId(id);
    }

    @Override
    public Object[] getInsertValues(ResenaImagen ri) {
        return new Object[] {
            ri.getResenaId(),
            ri.getUrl()
        };
    }

    @Override
    public Object[] getUpdateValues(ResenaImagen ri) {
        return new Object[] {
            ri.getResenaId(),
            ri.getUrl(),
            ri.getId()
        };
    }

    public List<ImagenResponse> findByResenaId(int resenaId) {

        String sql = """
            SELECT id, url
            FROM resena_imagenes
            WHERE resena_id = ?
            ORDER BY id ASC
        """;

        try {

            return DB.queryMany(con, sql,
                rs -> new ImagenResponse(
                    rs.getInt("id"),
                    rs.getString("url")
                ),
                resenaId
            );

        } catch (SQLException e) {
            throw new DataAccessException("Error obteniendo imágenes");
        }
    }
}