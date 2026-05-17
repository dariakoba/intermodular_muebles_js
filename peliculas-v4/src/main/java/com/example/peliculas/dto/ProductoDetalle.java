package com.example.peliculas.dto;

import com.example.peliculas.entity.Categoria;
import java.util.List;


public record ProductoDetalle(
        int idProducto,
        String nombre,
        String color,
        float precio,
        int stock,
        String descripcion,
        int categoriaId,
    	List<ImagenResponse> imagenes
) {}


