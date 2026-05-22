package com.dna.muebles.dto;

import java.time.LocalDateTime;
import java.util.List;


public record ProductoDetalle(
        int idProducto,
        String nombre,
        String color,
        float precio,
        int stock,
        String descripcion,
        int categoriaId,
        LocalDateTime deletedAt,

    	List<ImagenResponse> imagenes
) {}


