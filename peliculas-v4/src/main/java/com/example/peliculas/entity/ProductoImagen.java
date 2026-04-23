package com.example.peliculas.entity;

public class ProductoImagen {
	private Integer id;
	private Integer productoId;
	private String url;
	public ProductoImagen(Integer id, Integer productoId, String url) {
		super();
		this.id = id;
		this.productoId = productoId;
		this.url = url;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getProductoId() {
		return productoId;
	}
	public void setProductoId(Integer productoId) {
		this.productoId = productoId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "ProductoImagen [id=" + id + ", productoId=" + productoId + ", url=" + url + "]";
	}
	
	
}
