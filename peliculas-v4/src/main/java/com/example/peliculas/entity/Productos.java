	package com.example.peliculas.entity;
	
	
	public class Productos {
		 private int idProducto;
		    private String nombre;
		    private String color;
		    private float precio;
		    private int stock;
		    private String descripcion;
		    private Categoria categoria; // referencia a la entidad Categoria
	
	
	
		    // Constructor CORRECTO
		    public Producto(int idProducto, String nombre, String color, float precio,
		                    int stock, String descripcion, Categoria categoria) {
		        this.idProducto = idProducto;
		        this.nombre = nombre;
		        this.color = color;
		        this.precio = precio;
		        this.stock = stock;
		        this.descripcion = descripcion;
		        this.categoria = categoria;
		    }
	
	
		    // Getters y setters
		    public int getIdProducto() { return idProducto; }
		    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
	
		    public String getNombre() { return nombre; }
		    public void setNombre(String nombre) { this.nombre = nombre; }
	
		    public String getColor() { return color; }
		    public void setColor(String color) { this.color = color; }
	
		    public float getPrecio() { return precio; }
		    public void setPrecio(float precio) { this.precio = precio; }
	
		    public int getStock() { return stock; }
		    public void setStock(int stock) { this.stock = stock; }
	
		    public String getDescripcion() { return descripcion; }
		    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
	
		    public Categoria getCategoria() { return categoria; }
		    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
	
		    @Override
		    public String toString() {
		        return "Producto{" +
		                "idProducto=" + idProducto +
		                ", nombre='" + nombre + '\'' +
		                ", color='" + color + '\'' +
		                ", precio=" + precio +
		                ", stock=" + stock +
		                ", descripcion='" + descripcion + '\'' +
		                ", categoria=" + categoria +
		                '}';
		    }
	}
