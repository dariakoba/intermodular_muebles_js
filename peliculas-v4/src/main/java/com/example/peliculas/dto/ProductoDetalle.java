package com.example.peliculas.dto;

import com.example.peliculas.entity.Categoria;

public record ProductoDetalle(
        int id,
        String nombre,
        String color,
        float precio,
        int stock,
        String descripcion,
        String categoria
) {}
