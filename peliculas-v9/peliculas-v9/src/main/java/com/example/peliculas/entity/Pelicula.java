package com.example.peliculas.entity;

public class Pelicula {
	private Integer id;
    private String titulo;
    private Integer anyo;
    private Integer duracion;
    private String sinopsis;
    private Integer directorId;
    
	public Pelicula(Integer id, String titulo, Integer anyo, Integer duracion, String sinopsis, Integer directorId) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.anyo = anyo;
		this.duracion = duracion;
		this.sinopsis = sinopsis;
		this.directorId = directorId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Integer getAnyo() {
		return anyo;
	}

	public void setAnyo(Integer anyo) {
		this.anyo = anyo;
	}

	public Integer getDuracion() {
		return duracion;
	}

	public void setDuracion(Integer duracion) {
		this.duracion = duracion;
	}

	public String getSinopsis() {
		return sinopsis;
	}

	public void setSinopsis(String sinopsis) {
		this.sinopsis = sinopsis;
	}
	
	public Integer getDirectorId() {
		return directorId;
	}

	public void setDirectorId(Integer directorId) {
		this.directorId = directorId;
	}

	@Override
	public String toString() {
		return "Pelicula [id=" + id + ", titulo=" + titulo + ", anyo=" + anyo + ", duracion=" + duracion + ", sinopsis="
				+ sinopsis + ", directorId=" + directorId + "]";
	}
    
}
