package com.example.peliculas.entity;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Pedido {
    @JsonProperty("idPedido")
    private Integer idPedido;
    
    private LocalDate fecha;
    
    @JsonProperty("clienteNombre")
    private String clienteNombre;
    
    private float total;
    
    @JsonProperty("metodoPago")
    private String metodoPago;
    
    @JsonProperty("estadoPago")
    private String estadoPago;

    @JsonProperty("idProducto") 
    private Integer idProducto;

    @JsonProperty("nombreProducto")
    private String nombreProducto;

    public Pedido() {}

    // Getters y Setters
    public Integer getIdPedido() { return idPedido; }
    public void setIdPedido(Integer idPedido) { this.idPedido = idPedido; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public float getTotal() { return total; }
    public void setTotal(float total) { this.total = total; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }

    public Integer getIdProducto() { return idProducto; } // <--- NUEVO
    public void setIdProducto(Integer idProducto) { this.idProducto = idProducto; } // <--- NUEVO

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
}