package com.example.peliculas.dto;

import java.time.LocalDate;

public class VotoDTO {
    public int id;
    public String usuarioNombre;
    public int puntuacion;
    public String critica;
    public LocalDate fecha;

    public VotoDTO(int id, String usuarioNombre, int puntuacion, String critica, LocalDate fecha) {
        this.id = id;
        this.usuarioNombre = usuarioNombre;
        this.puntuacion = puntuacion;
        this.critica = critica;
        this.fecha = fecha;
    }
}
