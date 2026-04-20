package com.example.peliculas.entity;

import java.time.LocalDateTime;

public class Categoria {
	 private Integer idCategoria;
	 private String nombre;
	 private LocalDateTime deleted_at;
	 public Categoria(Integer idCategoria, String nombre, LocalDateTime deleted_at) {
		super();
		this.idCategoria = idCategoria;
		this.nombre = nombre;
		this.deleted_at = deleted_at;
	 }
	 public Integer getIdCategoria() {
		 return idCategoria;
	 }
	 public void setIdCategoria(Integer idCategoria) {
		 this.idCategoria = idCategoria;
	 }
	 public String getNombre() {
		 return nombre;
	 }
	 public void setNombre(String nombre) {
		 this.nombre = nombre;
	 }
	 public LocalDateTime getDeleted_at() {
		 return deleted_at;
	 }
	 public void setDeleted_at(LocalDateTime deleted_at) {
		 this.deleted_at = deleted_at;
	 }
	 @Override
	 public String toString() {
		return "Categoria [idCategoria=" + idCategoria + ", nombre=" + nombre + ", deleted_at=" + deleted_at + "]";
	 }

	 
	    
    
}
