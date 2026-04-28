package com.example.peliculas.dto;

import java.util.List;
import java.util.Map;
import com.example.peliculas.entity.Pedido;

public class CarritoRequest {
    
    private Pedido pedido;
    private List<Map<String, Object>> productos;
    private String direccion;
    private int puntosUsados;

    // --- Getters y Setters de TODO ---

    public Pedido getPedido() { 
        return pedido; 
    }
    
    public void setPedido(Pedido pedido) { 
        this.pedido = pedido; 
    }

    public List<Map<String, Object>> getProductos() { 
        return productos; 
    }
    
    public void setProductos(List<Map<String, Object>> productos) { 
        this.productos = productos; 
    }

    public String getDireccion() { 
        return direccion; 
    }

    public void setDireccion(String direccion) { 
        this.direccion = direccion; 
    }
    
    public int getPuntosUsados() { 
        return puntosUsados; 
    }

    public void setPuntosUsados(int puntosUsados) { 
        this.puntosUsados = puntosUsados; 
    }
}