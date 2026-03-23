package com.example.peliculas.dto;

import java.util.List;

public class ResenyaDTO {
    public int id;
    public String nombre;
    public double precio;
    public String descripcion;
    public String categoria;
    public Rating rating;
    public List<VotoDTO> ultimasResenyas;
    public UserVoto userVoto;
}