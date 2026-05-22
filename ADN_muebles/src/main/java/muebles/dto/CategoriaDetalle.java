package muebles.dto;

import muebles.entity.Categoria;

public record CategoriaDetalle(
        int id_categoria,
        String nombre,
        String estado
) {}
