package com.dna.muebles.dto;

import java.time.LocalDateTime;

import com.dna.muebles.entity.Categoria;

public record ProductoCatNomDetalle(
        int idProducto,
        String nombre,
        String color,
        float precio,
        int stock,
        String categoriaNombre,
        String estado
) {}


