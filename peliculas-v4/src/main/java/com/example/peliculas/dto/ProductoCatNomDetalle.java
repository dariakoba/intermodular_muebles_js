package com.example.peliculas.dto;

import java.time.LocalDateTime;

import com.example.peliculas.entity.Categoria;

public record ProductoCatNomDetalle(
        int idProducto,
        String nombre,
        String color,
        float precio,
        int stock,
        String categoriaNombre,
        String estado
) {}


