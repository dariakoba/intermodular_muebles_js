package com.example.peliculas.dto;

import java.time.LocalDate;

public class UserVoto {
    public int puntuacion;
    public String critica;
    public LocalDate fecha;

    public UserVoto(int puntuacion, String critica, LocalDate fecha) {
        this.puntuacion = puntuacion;
        this.critica = critica;
        this.fecha = fecha;
    }
}