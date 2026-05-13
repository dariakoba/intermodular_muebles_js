package com.example.peliculas.entity;

import java.time.LocalDate;

public class User {
    private Integer id;                   // id SERIAL -> int
    private String passwordHash;      // password_hash VARCHAR(255)
    private String rol;               // rol VARCHAR(20)
    private String telefono;          // telefono VARCHAR(20)
    private String estado;            // estado VARCHAR(20)
    private String nombre;            // nombre VARCHAR(50)
    private String apellidos;         // apellidos VARCHAR(50)
    private String direccion;         // direccion VARCHAR(100)
    private String email;
    private Integer puntos;       // antes int
    private Float salario;
    private java.time.LocalDate fechaAlta;
    
    public User() {}

	
	public User(Integer id, String passwordHash, String rol, String telefono, String estado, String nombre,
			String apellidos, String direccion, String email, Integer puntos,  Float salario,
			LocalDate fechaAlta) {
		super();
		this.id = id;
		this.passwordHash = passwordHash;
		this.rol = rol;
		this.telefono = telefono;
		this.estado = estado;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.direccion = direccion;
		this.email = email;
		this.puntos = puntos;
		this.salario = salario;
		this.fechaAlta = fechaAlta;
	}


	public User(String passwordHash, String rol, String telefono, String nombre,
            String apellidos, String email) {
    this.passwordHash = passwordHash;
    this.rol = rol;
    this.telefono = telefono;
    this.nombre = nombre;
    this.apellidos = apellidos;
    this.email = email;

    this.estado = "activo";
    this.direccion = "";
    this.puntos = 0;
    this.salario = 0f;
    this.fechaAlta = java.time.LocalDate.now();
	}
	public User(String nombre, String apellidos, String email, String rol, String passwordHash) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.rol = rol;
        this.passwordHash = passwordHash;
        this.estado = "activo";

        this.direccion = "";
        this.puntos = 0;
        this.salario = 0f;
        this.fechaAlta = java.time.LocalDate.now();
    }

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	public String getRol() {
		return rol;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getPuntos() {
		return puntos;
	}
	public void setPuntos(Integer puntos) {
		this.puntos = puntos;
	}
	
	public Float getSalario() {
		return salario;
	}
	public void setSalario(Float salario) {
		this.salario = salario;
	}
	public java.time.LocalDate getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(java.time.LocalDate fechaAlta) {
		this.fechaAlta = fechaAlta;
	}


	@Override
	public String toString() {
		return "User [id=" + id + ", passwordHash=" + passwordHash + ", rol=" + rol + ", telefono=" + telefono
				+ ", estado=" + estado + ", nombre=" + nombre + ", apellidos=" + apellidos + ", direccion=" + direccion
				+ ", email=" + email + ", puntos=" + puntos + ", salario=" + salario + ", fechaAlta=" + fechaAlta + "]";
	}
	
    
	
    
    

	    
	
}
