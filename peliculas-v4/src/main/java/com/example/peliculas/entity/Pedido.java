package com.example.peliculas.entity;
import java.time.LocalDate;

public class Pedido {
	private int idPedido;
    private LocalDate fechaPedido;
    private LocalDate fechaDevolucion;
    private float precio;
    private String metodoPago;
    private String factura;
    private String envio;
    private int clienteId;
	public Pedido(int idPedido, LocalDate fechaPedido, LocalDate fechaDevolucion, float precio, String metodoPago,
			String factura, String envio, int clienteId) {
		super();
		this.idPedido = idPedido;
		this.fechaPedido = fechaPedido;
		this.fechaDevolucion = fechaDevolucion;
		this.precio = precio;
		this.metodoPago = metodoPago;
		this.factura = factura;
		this.envio = envio;
		this.clienteId = clienteId;
	}
	public int getIdPedido() {
		return idPedido;
	}
	public void setId(int idPedido) {
		this.idPedido = idPedido;
	}
	public LocalDate getFechaPedido() {
		return fechaPedido;
	}
	public void setFechaPedido(LocalDate fechaPedido) {
		this.fechaPedido = fechaPedido;
	}
	public LocalDate getFechaDevolucion() {
		return fechaDevolucion;
	}
	public void setFechaDevolucion(LocalDate fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}
	public float getPrecio() {
		return precio;
	}
	public void setPrecio(float precio) {
		this.precio = precio;
	}
	public String getMetodoPago() {
		return metodoPago;
	}
	public void setMetodoPago(String metodoPago) {
		this.metodoPago = metodoPago;
	}
	public String getFactura() {
		return factura;
	}
	public void setFactura(String factura) {
		this.factura = factura;
	}
	public String getEnvio() {
		return envio;
	}
	public void setEnvio(String envio) {
		this.envio = envio;
	}
	public int getClienteId() {
		return clienteId;
	}
	public void setClienteId(int clienteId) {
		this.clienteId = clienteId;
	}
	@Override
	public String toString() {
		return "Pedido [id=" + idPedido + ", fechaPedido=" + fechaPedido + ", fechaDevolucion=" + fechaDevolucion
				+ ", precio=" + precio + ", metodoPago=" + metodoPago + ", factura=" + factura + ", envio=" + envio
				+ ", idCliente=" + clienteId + "]";
	}
    
    
}
