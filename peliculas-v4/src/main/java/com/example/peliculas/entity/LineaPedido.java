package com.example.peliculas.entity;

public class LineaPedido {
    private int idLineaPedido;
    private int cantidad;

    public LineaPedido() {}

    public LineaPedido(int idLineaPedido, int cantidad) {
        this.idLineaPedido = idLineaPedido;
        this.cantidad = cantidad;
    }

    public int getIdLineaPedido() { return cantidad; }
    public void setIdLineaPedido(int idLineaPedido) { this.idLineaPedido = idLineaPedido; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    @Override
    public String toString() {
        return "LineaPedido [idLineaPedido=" + idLineaPedido+  ", cantidad=" + cantidad + "]";
    }
}