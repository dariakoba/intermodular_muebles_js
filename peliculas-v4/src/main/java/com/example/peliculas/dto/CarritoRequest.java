package com.example.peliculas.dto;
import java.util.List;

import com.example.peliculas.entity.Pedido;

public class CarritoRequest {
    public Pedido pedido;
    public List<Integer> idsEjemplares; // Lista de IDs de los muebles elegidos
}
