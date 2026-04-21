package com.example.peliculas.dto;

import java.util.List;
import java.util.Map;
import com.example.peliculas.entity.Pedido;

public class CarritoRequest {
    public Pedido pedido;
    public List<Map<String, Object>> productos;

    public List<Map<String, Object>> getProductos() {
        return productos;
    }

    public Pedido getPedido() {
        return pedido;
    }
}