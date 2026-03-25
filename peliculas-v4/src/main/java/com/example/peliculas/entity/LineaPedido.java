package com.example.peliculas.entity;

public class LineaPedido {
    private int idPedido;
    private int idEjemplar;

    public LineaPedido() {}

    public LineaPedido(int idPedido, int idEjemplar) {
        this.idPedido = idPedido;
        this.idEjemplar = idEjemplar;
    }

    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public int getIdEjemplar() { return idEjemplar; }
    public void setIdEjemplar(int idEjemplar) { this.idEjemplar = idEjemplar; }

    @Override
    public String toString() {
        return "LineaPedido [idPedido=" + idPedido + ", idEjemplar=" + idEjemplar + "]";
    }
}