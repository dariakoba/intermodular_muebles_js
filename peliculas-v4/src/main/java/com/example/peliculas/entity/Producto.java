package com.example.peliculas.entity;

import java.time.LocalDateTime;

public class Producto {
	private Integer idProducto;
	private String nombre;
	private String color;
	private float precio;
	private int stock;
	private String descripcion;
	private Integer categoriaId; // referencia a la entidad Categoria
	private LocalDateTime deletedAt;

	

	public Producto(Integer idProducto, String nombre, String color, float precio, int stock, String descripcion,
			Integer categoriaId, LocalDateTime deletedAt) {
		super();
		this.idProducto = idProducto;
		this.nombre = nombre;
		this.color = color;
		this.precio = precio;
		this.stock = stock;
		this.descripcion = descripcion;
		this.categoriaId = categoriaId;
		this.deletedAt = deletedAt;
	}



	public Integer getIdProducto() {
		return idProducto;
	}



	public void setIdProducto(Integer idProducto) {
		this.idProducto = idProducto;
	}



	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public String getColor() {
		return color;
	}



	public void setColor(String color) {
		this.color = color;
	}



	public float getPrecio() {
		return precio;
	}



	public void setPrecio(float precio) {
		this.precio = precio;
	}



	public int getStock() {
		return stock;
	}



	public void setStock(int stock) {
		this.stock = stock;
	}



	public String getDescripcion() {
		return descripcion;
	}



	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



	public Integer getCategoriaId() {
		return categoriaId;
	}



	public void setCategoriaId(Integer categoriaId) {
		this.categoriaId = categoriaId;
	}



	public LocalDateTime getDeletedAt() {
		return deletedAt;
	}



	public void setDeletedAt(LocalDateTime deletedAt) {
		this.deletedAt = deletedAt;
	}



	@Override
	public String toString() {
		return "Producto [idProducto=" + idProducto + ", nombre=" + nombre + ", color=" + color + ", precio=" + precio
				+ ", stock=" + stock + ", descripcion=" + descripcion + ", categoriaId=" + categoriaId + ", deletedAt="
				+ deletedAt + "]";
	}

	

}