package com.example.peliculas.dto;

import com.example.peliculas.entity.Categoria;

public record CategoriaDetalle(
        int id,
        String nombre,
        String estado
) {}
