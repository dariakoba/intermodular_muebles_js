package com.example.peliculas.entity;

public class Director {
	private Integer id;
	private String nombre;
	private String pais;
	
	public Director(Integer id, String nombre, String pais) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.pais = pais;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	@Override
	public String toString() {
		return "Director [id=" + id + ", nombre=" + nombre + ", pais=" + pais + "]";
	}
	
}
