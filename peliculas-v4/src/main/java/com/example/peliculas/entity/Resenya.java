package com.example.peliculas.entity;

import java.time.LocalDate;

public class Resenya {
    private Integer idResenya; 
    private int usuarioId;
    private int productoId;
    private int puntuacion;
    private String comentario;
    private LocalDate fechaPublicacion;

    public Resenya() {}

    // Getters y Setters
   
    public Integer getIdResenya() {
		return idResenya;
	}

	public void setIdResenya(Integer idResenya) {
		this.idResenya = idResenya;
	}
    public int getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(int usuarioId) {
		this.usuarioId = usuarioId;
	}

	public int getProductoId() {
		return productoId;
	}

	public void setProductoId(int productoId) {
		this.productoId = productoId;
	}

	public int getPuntuacion() { return puntuacion; }
    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public LocalDate getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDate fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }
}