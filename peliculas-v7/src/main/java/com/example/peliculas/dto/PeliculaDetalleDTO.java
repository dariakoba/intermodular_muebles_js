package com.example.peliculas.dto;

import java.util.List;

public class PeliculaDetalleDTO {
	public int id;
	public String titulo;
	public int anyo;
	public int duracion;
	public String sinopsis;
	public String director;
	public Rating rating;
	public UserVote userVote;
	public List<VotoDTO> ultimosVotos;
}