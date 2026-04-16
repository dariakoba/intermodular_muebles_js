package com.example.peliculas.entity;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Pedido {
    private Integer idPedido;
    private LocalDate fecha;
    
    @JsonProperty("clienteNombre")
    private String clienteNombre;
    
    private float total;
    
    @JsonProperty("metodoPago")
    private String metodoPago;
    
    @JsonProperty("estadoPago")
    private String estadoPago;

    // 1. CONSTRUCTOR VACÍO (Ponlo siempre arriba, es vital para Spring)
    public Pedido() {
    }

    // 2. CONSTRUCTOR CON PARÁMETROS (Para tu Mapper)
    public Pedido(Integer idPedido, LocalDate fecha, String clienteNombre, float total, String metodoPago, String estadoPago) {
        this.idPedido = idPedido;
        this.fecha = fecha;
        this.clienteNombre = clienteNombre;
        this.total = total;
        this.metodoPago = metodoPago;
        this.estadoPago = estadoPago;
    }

    // 3. GETTERS Y SETTERS (Asegúrate de que los nombres coincidan exactamente)
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
}