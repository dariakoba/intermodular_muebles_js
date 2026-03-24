package com.example.peliculas.dto;

import java.time.LocalDate;

public class UserVoto {
    public int puntuacion;
    public String critica;
    public LocalDate fecha;

    // Este constructor es el que invocas en image_743868.png
    public UserVoto(int puntuacion, String critica, LocalDate fecha) {
        this.puntuacion = puntuacion;
        this.critica = critica;
        this.fecha = fecha;
    }
}