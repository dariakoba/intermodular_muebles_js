package com.example.peliculas.entity;

import java.time.LocalDate;

public class Resenya {
    private Integer idResenya;
    private int idCliente;
    private int idProducto;
    private int puntuacion;
    private String comentario;
    private LocalDate fechaResenya;

    public Resenya() {}

    // Getters y Setters
    public Integer getIdResenya() { return idResenya; }
    public void setIdResenya(Integer idResenya) { this.idResenya = idResenya; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public int getPuntuacion() { return puntuacion; }
    public void setPuntuacion(int puntuacion) { this.puntuacion = puntuacion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public LocalDate getFechaResenya() { return fechaResenya; }
    public void setFechaResenya(LocalDate fechaResenya) { this.fechaResenya = fechaResenya; }
}