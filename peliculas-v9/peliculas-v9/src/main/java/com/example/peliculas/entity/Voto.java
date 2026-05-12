package com.example.peliculas.entity;

import java.time.LocalDate;

public class Voto {
	private Integer id;
	private Integer peliculaId;
	private Integer userId;
	private int puntuacion;
	private String critica;
	private LocalDate fecha;
	
	public Voto(Integer id, Integer peliculaId, Integer userId, int puntuacion, String critica, LocalDate fecha) {
		super();
		this.id = id;
		this.peliculaId = peliculaId;
		this.userId = userId;
		this.puntuacion = puntuacion;
		this.critica = critica;
		this.fecha = fecha;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPeliculaId() {
		return peliculaId;
	}

	public void setPeliculaId(Integer peliculaId) {
		this.peliculaId = peliculaId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public int getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}

	public String getCritica() {
		return critica;
	}

	public void setCritica(String critica) {
		this.critica = critica;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	@Override
	public String toString() {
		return "Voto [id=" + id + ", peliculaId=" + peliculaId + ", userId=" + userId + ", puntuacion=" + puntuacion
				+ ", critica=" + critica + ", fecha=" + fecha + "]";
	}
	
}
