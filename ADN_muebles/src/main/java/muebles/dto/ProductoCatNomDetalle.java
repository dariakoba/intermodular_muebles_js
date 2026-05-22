package muebles.dto;

import java.time.LocalDateTime;

import muebles.entity.Categoria;

public record ProductoCatNomDetalle(
        int idProducto,
        String nombre,
        String color,
        float precio,
        int stock,
        String categoriaNombre,
        String estado
) {}


