package com.dna.muebles.dto;

import com.dna.muebles.entity.Categoria;

public record CategoriaDetalle(
        int id_categoria,
        String nombre,
        String estado
) {}
