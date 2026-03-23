package com.example.peliculas.dto;

import com.example.peliculas.entity.Categoria;

public record ProductoDetalle(
        int idProducto,
        String nombre,
        String color,
        float precio,
        int stock,
        String descripcion,
        Categoria categoriaId
) {}
